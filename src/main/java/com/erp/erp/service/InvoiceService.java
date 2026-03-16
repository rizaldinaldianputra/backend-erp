package com.erp.erp.service;

import com.erp.erp.dto.sales.InvoiceRequest;
import com.erp.erp.model.Invoice;
import com.erp.erp.model.SalesOrder;
import com.erp.erp.repository.InvoiceRepository;
import com.erp.erp.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.erp.erp.util.CodeGeneratorUtil;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final AccountingService accountingService;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            SalesOrderRepository salesOrderRepository,
            AccountingService accountingService,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.invoiceRepository = invoiceRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.accountingService = accountingService;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public Invoice createInvoice(InvoiceRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findById(request.getSalesOrderId())
                .orElseThrow(() -> new RuntimeException("Sales Order not found"));

        if (salesOrder.getStatus() != SalesOrder.Status.CONFIRMED
                && salesOrder.getStatus() != SalesOrder.Status.SHIPPED) {
            // Allow creating invoice if confirmed or shipped
        }

        Invoice invoice = new Invoice();
        if (invoice.getCode() == null || invoice.getCode().isEmpty()) {
            invoice.setCode(codeGeneratorUtil.generateCode("INV"));
        }
        invoice.setSalesOrder(salesOrder);
        invoice.setCustomer(salesOrder.getCustomer());
        invoice.setDate(request.getDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(Invoice.Status.DRAFT);

        // Copy totals from SO
        invoice.setSubTotal(salesOrder.getSubTotal());
        invoice.setTaxAmount(salesOrder.getTaxAmount());
        invoice.setDiscountAmount(salesOrder.getDiscountAmount());
        invoice.setTotalAmount(salesOrder.getTotalAmount());
        invoice.setPaidAmount(BigDecimal.ZERO);

        Invoice saved = invoiceRepository.save(invoice);

        // Update SO status
        salesOrder.setStatus(SalesOrder.Status.INVOICED);
        salesOrderRepository.save(salesOrder);

        return saved;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice updateStatus(Long id, Invoice.Status status) {
        Invoice invoice = getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Trigger accounting if moving to SENT (Finalized)
        if (invoice.getStatus() == Invoice.Status.DRAFT && status == Invoice.Status.SENT) {
            accountingService.createJournalFromInvoice(invoice);
        }

        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
}
