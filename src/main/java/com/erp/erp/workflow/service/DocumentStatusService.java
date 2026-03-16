package com.erp.erp.workflow.service;

import com.erp.erp.model.*;
import com.erp.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Central service that delegates status updates to the correct repository
 * based on documentType. Used by Camunda delegates.
 *
 * All entities use typed enums, so we convert the String status to the correct
 * enum.
 */
@Service
@Transactional
public class DocumentStatusService {

    private final QuotationRepository quotationRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final SalesReturnRepository salesReturnRepository;
    private final PurchaseReturnRepository purchaseReturnRepository;
    private final BudgetRepository budgetRepository;

    public DocumentStatusService(
            QuotationRepository quotationRepository,
            SalesOrderRepository salesOrderRepository,
            InvoiceRepository invoiceRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            GoodsReceiptRepository goodsReceiptRepository,
            PurchaseInvoiceRepository purchaseInvoiceRepository,
            SupplierPaymentRepository supplierPaymentRepository,
            PaymentRepository paymentRepository,
            DeliveryOrderRepository deliveryOrderRepository,
            SalesReturnRepository salesReturnRepository,
            PurchaseReturnRepository purchaseReturnRepository,
            BudgetRepository budgetRepository) {
        this.quotationRepository = quotationRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.purchaseInvoiceRepository = purchaseInvoiceRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.paymentRepository = paymentRepository;
        this.deliveryOrderRepository = deliveryOrderRepository;
        this.salesReturnRepository = salesReturnRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.budgetRepository = budgetRepository;
    }

    public void setStatus(String documentType, Long documentId, String status) {
        switch (documentType) {
            case "QUOTATION" -> {
                Quotation q = quotationRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("Quotation not found: " + documentId));
                q.setStatus(Quotation.Status.valueOf(status));
                quotationRepository.save(q);
            }
            case "SALES_ORDER" -> {
                SalesOrder so = salesOrderRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("SalesOrder not found: " + documentId));
                so.setStatus(SalesOrder.Status.valueOf(status));
                salesOrderRepository.save(so);
            }
            case "INVOICE" -> {
                Invoice inv = invoiceRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("Invoice not found: " + documentId));
                inv.setStatus(Invoice.Status.valueOf(status));
                invoiceRepository.save(inv);
            }
            case "PURCHASE_ORDER" -> {
                PurchaseOrder po = purchaseOrderRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("PurchaseOrder not found: " + documentId));
                po.setStatus(status); // PurchaseOrder uses String status
                purchaseOrderRepository.save(po);
            }
            case "GOODS_RECEIPT" -> {
                GoodsReceipt gr = goodsReceiptRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("GoodsReceipt not found: " + documentId));
                gr.setStatus(GoodsReceipt.Status.valueOf(status));
                goodsReceiptRepository.save(gr);
            }
            case "PURCHASE_INVOICE" -> {
                PurchaseInvoice pi = purchaseInvoiceRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("PurchaseInvoice not found: " + documentId));
                pi.setStatus(PurchaseInvoice.Status.valueOf(status));
                purchaseInvoiceRepository.save(pi);
            }
            case "SUPPLIER_PAYMENT" -> {
                SupplierPayment sp = supplierPaymentRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("SupplierPayment not found: " + documentId));
                sp.setStatus(SupplierPayment.Status.valueOf(status));
                supplierPaymentRepository.save(sp);
            }
            case "PAYMENT" -> {
                Payment pay = paymentRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("Payment not found: " + documentId));
                pay.setStatus(Payment.Status.valueOf(status));
                paymentRepository.save(pay);
            }
            case "DELIVERY_ORDER" -> {
                DeliveryOrder doObj = deliveryOrderRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("DeliveryOrder not found: " + documentId));
                doObj.setStatus(DeliveryOrder.Status.valueOf(status));
                deliveryOrderRepository.save(doObj);
            }
            case "SALES_RETURN" -> {
                SalesReturn sr = salesReturnRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("SalesReturn not found: " + documentId));
                sr.setStatus(SalesReturn.Status.valueOf(status));
                salesReturnRepository.save(sr);
            }
            case "PURCHASE_RETURN" -> {
                PurchaseReturn pr = purchaseReturnRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("PurchaseReturn not found: " + documentId));
                pr.setStatus(PurchaseReturn.Status.valueOf(status));
                purchaseReturnRepository.save(pr);
            }
            case "BUDGET" -> {
                Budget bg = budgetRepository.findById(documentId)
                        .orElseThrow(() -> new RuntimeException("Budget not found: " + documentId));
                bg.setStatus(Budget.Status.valueOf(status));
                budgetRepository.save(bg);
            }
            default -> throw new RuntimeException("Unknown document type: " + documentType);
        }
    }

    public java.math.BigDecimal getDocumentAmount(String documentType, Long documentId) {
        return switch (documentType) {
            case "QUOTATION" -> quotationRepository.findById(documentId)
                    .map(Quotation::getTotalAmount).orElse(java.math.BigDecimal.ZERO);
            case "SALES_ORDER" -> salesOrderRepository.findById(documentId)
                    .map(SalesOrder::getTotalAmount).orElse(java.math.BigDecimal.ZERO);
            case "PURCHASE_ORDER" -> purchaseOrderRepository.findById(documentId)
                    .map(PurchaseOrder::getTotalAmount).orElse(java.math.BigDecimal.ZERO);
            case "INVOICE" -> invoiceRepository.findById(documentId)
                    .map(inv -> inv.getTotalAmount()).orElse(java.math.BigDecimal.ZERO);
            // Add other documents as needed. Defaulting to zero if not implemented or no amount field.
            default -> java.math.BigDecimal.ZERO;
        };
    }

    public String getApprovedStatus(String documentType) {
        return switch (documentType) {
            case "QUOTATION" -> "APPROVED";
            case "SALES_ORDER" -> "CONFIRMED";
            case "INVOICE" -> "SENT";
            case "PURCHASE_ORDER" -> "APPROVED";
            case "GOODS_RECEIPT" -> "VERIFIED";
            case "PAYMENT" -> "APPROVED";
            case "DELIVERY_ORDER" -> "APPROVED";
            case "SALES_RETURN" -> "APPROVED";
            case "PURCHASE_RETURN" -> "APPROVED";
            case "BUDGET" -> "APPROVED";
            default -> "APPROVED";
        };
    }
}
