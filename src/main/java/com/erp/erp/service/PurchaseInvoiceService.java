package com.erp.erp.service;

import com.erp.erp.dto.purchase.PurchaseInvoiceRequest;
import com.erp.erp.model.PurchaseInvoice;
import com.erp.erp.model.PurchaseOrder;
import com.erp.erp.repository.PurchaseInvoiceRepository;
import com.erp.erp.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.erp.erp.util.CodeGeneratorUtil;

@Service
@Transactional
public class PurchaseInvoiceService {

    private final PurchaseInvoiceRepository piRepository;
    private final PurchaseOrderRepository poRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;
    private final AccountingService accountingService;

    public PurchaseInvoiceService(
            PurchaseInvoiceRepository piRepository,
            PurchaseOrderRepository poRepository,
            CodeGeneratorUtil codeGeneratorUtil,
            AccountingService accountingService) {
        this.piRepository = piRepository;
        this.poRepository = poRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
        this.accountingService = accountingService;
    }

    public PurchaseInvoice createPurchaseInvoice(PurchaseInvoiceRequest request) {
        PurchaseOrder po = poRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));

        if (!"OPEN".equalsIgnoreCase(po.getStatus()) && !"APPROVED".equalsIgnoreCase(po.getStatus())) {
            // Check status logic. Usually PO must be Approved.
        }

        PurchaseInvoice invoice = new PurchaseInvoice();
        invoice.setCode(codeGeneratorUtil.generateCode("PI"));
        invoice.setPurchaseOrder(po);
        invoice.setSupplier(po.getSupplier());
        invoice.setDate(request.getDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(PurchaseInvoice.Status.DRAFT);

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (po.getItems() != null) {
            for (var item : po.getItems()) {
                BigDecimal lineTotal = BigDecimal.valueOf(item.getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(lineTotal);
            }
        }

        invoice.setSubTotal(totalAmount);
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setDiscountAmount(BigDecimal.ZERO);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaidAmount(BigDecimal.ZERO);

        PurchaseInvoice savedInvoice = piRepository.save(invoice);

        // Trigger Journal Creation
        accountingService.createJournalFromPurchaseInvoice(savedInvoice);

        return savedInvoice;
    }

    public List<PurchaseInvoice> getAllPurchaseInvoices() {
        return piRepository.findAll();
    }

    public Optional<PurchaseInvoice> getPurchaseInvoiceById(Long id) {
        return piRepository.findById(id);
    }

    public PurchaseInvoice updateStatus(Long id, PurchaseInvoice.Status status) {
        PurchaseInvoice invoice = getPurchaseInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(status);
        return piRepository.save(invoice);
    }
}
