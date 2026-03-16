package com.erp.erp.service;

import com.erp.erp.dto.sales.PaymentRequest;
import com.erp.erp.model.Invoice;
import com.erp.erp.model.Payment;
import com.erp.erp.repository.InvoiceRepository;
import com.erp.erp.repository.PaymentRepository;
import com.erp.erp.util.CodeGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final AccountingService accountingService;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public PaymentService(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository,
            AccountingService accountingService,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.accountingService = accountingService;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public Payment createPayment(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() == Invoice.Status.PAID || invoice.getStatus() == Invoice.Status.CANCELLED) {
            throw new RuntimeException("Invoice is already paid or cancelled");
        }

        Payment payment = new Payment();
        if (payment.getCode() == null || payment.getCode().isEmpty()) {
            payment.setCode(codeGeneratorUtil.generateCode("PAY"));
        }
        payment.setInvoice(invoice);
        payment.setCustomer(invoice.getCustomer());
        payment.setDate(request.getDate());
        payment.setAmount(request.getAmount());
        payment.setNote(request.getNote());
        payment.setReceiptUrl(request.getReceiptUrl());
        try {
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment method");
        }

        Payment savedPayment = paymentRepository.save(payment);

        // Update Invoice
        BigDecimal newPaidAmount = invoice.getPaidAmount().add(request.getAmount());
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) > 0) {
            throw new RuntimeException("Payment amount exceeds remaining invoice amount");
        }
        invoice.setPaidAmount(newPaidAmount);

        if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(Invoice.Status.PAID);
        } else {
            invoice.setStatus(Invoice.Status.PARTIALLY_PAID);
        }
        invoiceRepository.save(invoice);

        // Trigger Accounting
        accountingService.createJournalFromPayment(savedPayment);

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Revert invoice status
        Invoice invoice = payment.getInvoice();
        invoice.setPaidAmount(invoice.getPaidAmount().subtract(payment.getAmount()));
        if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) < 0) {
            invoice.setPaidAmount(BigDecimal.ZERO);
        }

        if (invoice.getPaidAmount().compareTo(invoice.getTotalAmount()) < 0) {
            if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
                invoice.setStatus(Invoice.Status.SENT);
            } else {
                invoice.setStatus(Invoice.Status.PARTIALLY_PAID);
            }
        }

        invoiceRepository.save(invoice);
        paymentRepository.delete(payment);
    }
}
