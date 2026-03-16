package com.erp.erp.service;

import com.erp.erp.model.*;
import com.erp.erp.repository.ChartOfAccountRepository;
import com.erp.erp.repository.JournalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountingService {

    private final JournalRepository journalRepository;
    private final ChartOfAccountRepository coaRepository;

    public AccountingService(JournalRepository journalRepository, ChartOfAccountRepository coaRepository) {
        this.journalRepository = journalRepository;
        this.coaRepository = coaRepository;
    }

    public Page<JournalHeader> getAllJournals(Pageable pageable) {
        return journalRepository.findAll(pageable);
    }

    public Optional<JournalHeader> getJournalById(Long id) {
        return journalRepository.findById(id);
    }

    @Transactional
    public JournalHeader createJournal(JournalHeader journal) {
        if (journal.getId() != null) {
            throw new IllegalArgumentException("New journal should not have an ID");
        }

        // Auto-generate document number if empty
        if (journal.getDocumentNumber() == null || journal.getDocumentNumber().isBlank()) {
            journal.setDocumentNumber("JV-" + System.currentTimeMillis());
        }

        if (journal.getDetails() != null) {
            for (JournalDetail detail : journal.getDetails()) {
                detail.setHeader(journal);
            }
        }

        validateBalance(journal);

        return journalRepository.save(journal);
    }

    @Transactional
    public void createJournalFromInvoice(Invoice invoice) {
        JournalHeader journal = new JournalHeader();
        journal.setDocumentNumber("JV-INV-" + invoice.getCode());
        journal.setTransactionDate(invoice.getDate());
        journal.setDescription("Sales Invoice " + invoice.getCode());
        journal.setReferenceNumber(invoice.getCode());
        journal.setStatus(JournalHeader.JournalStatus.POSTED);

        List<JournalDetail> details = new ArrayList<>();

        // Debit: Accounts Receivable (Piutang)
        ChartOfAccount arAccount = getCoaByCodeOrThrow("1201");
        details.add(JournalDetail.builder()
                .header(journal)
                .chartOfAccount(arAccount)
                .description("AR - " + invoice.getCustomer().getName())
                .debit(invoice.getTotalAmount())
                .credit(BigDecimal.ZERO)
                .build());

        // Credit: Sales Revenue (Penjualan)
        ChartOfAccount salesAccount = getCoaByCodeOrThrow("4001");
        details.add(JournalDetail.builder()
                .header(journal)
                .chartOfAccount(salesAccount)
                .description("Sales Revenue - " + invoice.getCode())
                .debit(BigDecimal.ZERO)
                .credit(invoice.getTotalAmount()) // Simplified: Total to Sales. Ideally separate Tax.
                .build());

        journal.setDetails(details);
        journalRepository.save(journal);
    }

    @Transactional
    public void createJournalFromPayment(Payment payment) {
        JournalHeader journal = new JournalHeader();
        journal.setDocumentNumber("JV-PAY-" + payment.getCode());
        journal.setTransactionDate(payment.getDate());
        journal.setDescription("Payment Received " + payment.getCode());
        journal.setReferenceNumber(payment.getCode());
        journal.setStatus(JournalHeader.JournalStatus.POSTED);

        List<JournalDetail> details = new ArrayList<>();

        // Debit: Bank/Cash
        // Ensure getCoaByCodeOrThrow handles missing accounts gracefully or we seed
        // them
        ChartOfAccount bankAccount = getCoaByCodeOrThrow("1101");
        details.add(JournalDetail.builder()
                .header(journal)
                .chartOfAccount(bankAccount)
                .description("Payment from " + payment.getCustomer().getName())
                .debit(payment.getAmount())
                .credit(BigDecimal.ZERO)
                .build());

        // Credit: Accounts Receivable
        ChartOfAccount arAccount = getCoaByCodeOrThrow("1201");
        details.add(JournalDetail.builder()
                .header(journal)
                .chartOfAccount(arAccount)
                .description("Payment for " + payment.getInvoice().getCode())
                .debit(BigDecimal.ZERO)
                .credit(payment.getAmount())
                .build());

        journal.setDetails(details);
        journalRepository.save(journal);
    }

    @Transactional
    public void createJournalFromPurchaseInvoice(PurchaseInvoice invoice) {
        JournalHeader journal = new JournalHeader();
        journal.setDocumentNumber("JV-PI-" + invoice.getCode());
        journal.setTransactionDate(invoice.getDate());
        journal.setDescription("Purchase Invoice " + invoice.getCode());
        journal.setReferenceNumber(invoice.getCode());
        journal.setStatus(JournalHeader.JournalStatus.POSTED);

        List<JournalDetail> details = new ArrayList<>();

        // Debit: Inventory / Expense
        // Depending on module, usually Purchase means Inventory (1105) or explicit
        // Expense.
        ChartOfAccount inventoryAccount = getCoaByCodeOrThrow("1105"); // Example: Raw Material Inventory
        details.add(JournalDetail.builder()
                .header(journal)
                .chartOfAccount(inventoryAccount)
                .description("Purchase - " + invoice.getSupplier().getName())
                .debit(invoice.getTotalAmount())
                .credit(BigDecimal.ZERO)
                .build());

        // Credit: Accounts Payable (Hutang)
        ChartOfAccount apAccount = getCoaByCodeOrThrow("2101");
        details.add(JournalDetail.builder()
                .header(journal)
                .chartOfAccount(apAccount)
                .description("AP - " + invoice.getCode())
                .debit(BigDecimal.ZERO)
                .credit(invoice.getTotalAmount())
                .build());

        journal.setDetails(details);
        journalRepository.save(journal);
    }

    @Transactional
    public void createJournalFromDeliveryOrder(DeliveryOrder deliveryOrder) {
        JournalHeader journal = new JournalHeader();
        journal.setDocumentNumber("JV-DO-" + deliveryOrder.getCode());
        journal.setTransactionDate(deliveryOrder.getDeliveryDate());
        journal.setDescription("Delivery Order " + deliveryOrder.getCode());
        journal.setReferenceNumber(deliveryOrder.getCode());
        journal.setStatus(JournalHeader.JournalStatus.POSTED);

        List<JournalDetail> details = new ArrayList<>();

        // Calculate total cost (simplified, using price if cost not available)
        BigDecimal totalCost = BigDecimal.ZERO;
        for (DeliveryOrderItem item : deliveryOrder.getItems()) {
            double costPriceVal = 0.0;
            if (item.getProduct().getCostPrice() != null) {
                costPriceVal = item.getProduct().getCostPrice().doubleValue();
            }
            BigDecimal itemCost = BigDecimal.valueOf(costPriceVal);

            Integer qty = item.getDeliveredQuantity() != null && item.getDeliveredQuantity() > 0
                    ? item.getDeliveredQuantity()
                    : item.getQuantity();
            totalCost = totalCost.add(itemCost.multiply(BigDecimal.valueOf(qty)));
        }

        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            // Debit: Cost of Goods Sold (HPP)
            ChartOfAccount cogsAccount = getCoaByCodeOrThrow("5001");
            details.add(JournalDetail.builder()
                    .header(journal)
                    .chartOfAccount(cogsAccount)
                    .description("COGS - DO " + deliveryOrder.getCode())
                    .debit(totalCost)
                    .credit(BigDecimal.ZERO)
                    .build());

            // Credit: Inventory
            ChartOfAccount inventoryAccount = getCoaByCodeOrThrow("1105");
            details.add(JournalDetail.builder()
                    .header(journal)
                    .chartOfAccount(inventoryAccount)
                    .description("Inventory OUT - DO " + deliveryOrder.getCode())
                    .debit(BigDecimal.ZERO)
                    .credit(totalCost)
                    .build());

            journal.setDetails(details);
            journalRepository.save(journal);
        }
    }

    @Transactional
    public void createJournalFromGoodsReceipt(GoodsReceipt goodsReceipt) {
        JournalHeader journal = new JournalHeader();
        journal.setDocumentNumber("JV-GR-" + goodsReceipt.getCode());
        journal.setTransactionDate(goodsReceipt.getDate());
        journal.setDescription("Goods Receipt " + goodsReceipt.getCode());
        journal.setReferenceNumber(goodsReceipt.getCode());
        journal.setStatus(JournalHeader.JournalStatus.POSTED);

        List<JournalDetail> details = new ArrayList<>();

        BigDecimal totalCost = BigDecimal.ZERO;
        for (GoodsReceiptItem item : goodsReceipt.getItems()) {
            BigDecimal itemCost = item.getProduct().getCostPrice() != null ? item.getProduct().getCostPrice()
                    : BigDecimal.ZERO;
            totalCost = totalCost.add(itemCost.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            // Debit: Inventory
            ChartOfAccount inventoryAccount = getCoaByCodeOrThrow("1105");
            details.add(JournalDetail.builder()
                    .header(journal)
                    .chartOfAccount(inventoryAccount)
                    .description("Inventory IN - GR " + goodsReceipt.getCode())
                    .debit(totalCost)
                    .credit(BigDecimal.ZERO)
                    .build());

            // Credit: Goods Receipt Not Invoiced (Hutang Belum Ditagih) or AP
            ChartOfAccount unbilledAccount = getCoaByCodeOrThrow("2102"); // Adjust code as needed
            details.add(JournalDetail.builder()
                    .header(journal)
                    .chartOfAccount(unbilledAccount)
                    .description("Unbilled Receipts - GR " + goodsReceipt.getCode())
                    .debit(BigDecimal.ZERO)
                    .credit(totalCost)
                    .build());

            journal.setDetails(details);
            journalRepository.save(journal);
        }
    }

    private ChartOfAccount getCoaByCodeOrThrow(String code) {
        // Fallback or throw if not found. For now, throw to signal data issue.
        // In real app, might want to create default or look for "Unmapped"
        return coaRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Chart of Account not found for code: " + code));
    }

    private void validateBalance(JournalHeader journal) {
        if (journal.getDetails() == null || journal.getDetails().isEmpty()) {
            return;
        }

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (JournalDetail detail : journal.getDetails()) {
            if (detail.getDebit() != null)
                totalDebit = totalDebit.add(detail.getDebit());
            if (detail.getCredit() != null)
                totalCredit = totalCredit.add(detail.getCredit());
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalArgumentException("Journal is not balanced");
        }
    }

    @Transactional
    public JournalHeader postJournal(Long id) {
        JournalHeader journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (journal.getStatus() == JournalHeader.JournalStatus.POSTED) {
            throw new IllegalArgumentException("Journal already posted");
        }

        validateBalance(journal);
        journal.setStatus(JournalHeader.JournalStatus.POSTED);

        return journalRepository.save(journal);
    }
}
