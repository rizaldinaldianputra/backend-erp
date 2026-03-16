package com.erp.erp.service;

import com.erp.erp.dto.purchase.SupplierPaymentRequest;
import com.erp.erp.model.PurchaseInvoice;
import com.erp.erp.model.SupplierPayment;
import com.erp.erp.repository.PurchaseInvoiceRepository;
import com.erp.erp.repository.SupplierPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.erp.erp.util.CodeGeneratorUtil;

@Service
@Transactional
public class SupplierPaymentService {

    private final SupplierPaymentRepository spRepository;
    private final PurchaseInvoiceRepository piRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public SupplierPaymentService(
            SupplierPaymentRepository spRepository,
            PurchaseInvoiceRepository piRepository,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.spRepository = spRepository;
        this.piRepository = piRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public SupplierPayment createSupplierPayment(SupplierPaymentRequest request) {
        PurchaseInvoice invoice = piRepository.findById(request.getPurchaseInvoiceId())
                .orElseThrow(() -> new RuntimeException("Purchase Invoice not found"));

        if (invoice.getStatus() == PurchaseInvoice.Status.PAID
                || invoice.getStatus() == PurchaseInvoice.Status.CANCELLED) {
            throw new RuntimeException("Invoice is already paid or cancelled");
        }

        SupplierPayment payment = new SupplierPayment();
        if (payment.getCode() == null || payment.getCode().isEmpty()) {
            payment.setCode(codeGeneratorUtil.generateCode("SP"));
        }
        payment.setPurchaseInvoice(invoice);
        payment.setSupplier(invoice.getSupplier());
        payment.setDate(request.getDate());
        payment.setAmount(request.getAmount());
        payment.setNote(request.getNote());

        try {
            payment.setPaymentMethod(SupplierPayment.PaymentMethod.valueOf(request.getPaymentMethod()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment method");
        }

        SupplierPayment savedPayment = spRepository.save(payment);

        // Update Invoice
        BigDecimal newPaidAmount = invoice.getPaidAmount().add(request.getAmount());
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) > 0) {
            throw new RuntimeException("Payment amount exceeds remaining invoice amount");
        }
        invoice.setPaidAmount(newPaidAmount);

        if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(PurchaseInvoice.Status.PAID);
        } else {
            invoice.setStatus(PurchaseInvoice.Status.PARTIALLY_PAID);
        }
        piRepository.save(invoice);

        return savedPayment;
    }

    public List<SupplierPayment> getAllSupplierPayments() {
        return spRepository.findAll();
    }

    public Optional<SupplierPayment> getSupplierPaymentById(Long id) {
        return spRepository.findById(id);
    }
}
