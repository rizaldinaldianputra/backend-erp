package com.erp.erp.service;

import com.erp.erp.model.SalesOrder;
import com.erp.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

        private final SalesOrderRepository salesOrderRepository;
        private final InventoryRepository inventoryRepository;
        private final ProductRepository productRepository;
        private final WorkOrderRepository workOrderRepository;
        private final InvoiceRepository invoiceRepository;
        private final PurchaseInvoiceRepository purchaseInvoiceRepository;

        public Map<String, Object> getDashboardStats() {
                Map<String, Object> stats = new HashMap<>();

                // 1. Production Stats
                long totalProduction = workOrderRepository.findAll().stream()
                                .filter(wo -> wo.getStatus() == com.erp.erp.model.WorkOrder.Status.COMPLETED)
                                .mapToLong(wo -> wo.getProducedQuantity())
                                .sum();
                stats.put("totalProduction", totalProduction);

                // 2. Sales Stats
                List<SalesOrder> allSales = salesOrderRepository.findAll();
                long totalOrders = allSales.stream()
                                .filter(so -> so.getStatus() != com.erp.erp.model.SalesOrder.Status.CANCELLED)
                                .mapToLong(so -> so.getItems().stream().mapToLong(item -> item.getQuantity()).sum())
                                .sum();
                stats.put("totalOrders", totalOrders);

                // 3. Inventory Stats
                long lowStockCount = inventoryRepository.findAll().stream()
                                .filter(inv -> inv.getQuantity() < 10)
                                .count();
                stats.put("lowStockItems", lowStockCount);
                stats.put("totalProducts", productRepository.count());

                // 4. Placeholders for complex metrics
                stats.put("inventoryTurnover", 8.5); // Placeholder
                stats.put("onTimeDelivery", 95); // Placeholder

                return stats;
        }

        public List<Map<String, Object>> getProductionVsOrdersData() {
                // Simple 6 month aggregation mock-ish but based on real data counts
                String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                                "Dec" };
                List<Map<String, Object>> data = new ArrayList<>();

                int currentMonth = LocalDate.now().getMonthValue();
                for (int i = currentMonth - 5; i <= currentMonth; i++) {
                        int m = (i <= 0) ? i + 12 : i;
                        String monthName = months[m - 1];

                        long production = workOrderRepository.findAll().stream()
                                        .filter(wo -> wo.getCreatedDate() != null
                                                        && wo.getCreatedDate().getMonthValue() == m)
                                        .mapToLong(wo -> wo.getProducedQuantity())
                                        .sum();

                        long orders = salesOrderRepository.findAll().stream()
                                        .filter(so -> so.getDate() != null && so.getDate().getMonthValue() == m)
                                        .count();

                        Map<String, Object> entry = new HashMap<>();
                        entry.put("name", monthName);
                        entry.put("production", production);
                        entry.put("orders", orders);
                        data.add(entry);
                }
                return data;
        }

        public List<Map<String, Object>> getRevenueVsCostData() {
                String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                                "Dec" };
                List<Map<String, Object>> data = new ArrayList<>();

                int currentMonth = LocalDate.now().getMonthValue();
                for (int i = currentMonth - 5; i <= currentMonth; i++) {
                        int m = (i <= 0) ? i + 12 : i;
                        String monthName = months[m - 1];

                        BigDecimal revenue = invoiceRepository.findAll().stream()
                                        .filter(inv -> inv.getDate() != null && inv.getDate().getMonthValue() == m)
                                        .map(inv -> inv.getTotalAmount() != null ? inv.getTotalAmount()
                                                        : BigDecimal.ZERO)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        BigDecimal cost = purchaseInvoiceRepository.findAll().stream()
                                        .filter(pi -> pi.getDate() != null && pi.getDate().getMonthValue() == m)
                                        .map(pi -> pi.getTotalAmount() != null ? pi.getTotalAmount() : BigDecimal.ZERO)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        Map<String, Object> entry = new HashMap<>();
                        entry.put("name", monthName);
                        entry.put("revenue", revenue);
                        entry.put("cost", cost);
                        data.add(entry);
                }
                return data;
        }

        public List<Map<String, Object>> getProductDistributionData() {
                return productRepository.findAll().stream()
                                .filter(p -> p.getCategory() != null)
                                .collect(java.util.stream.Collectors.groupingBy(p -> p.getCategory().getName(),
                                                java.util.stream.Collectors.counting()))
                                .entrySet().stream()
                                .map(e -> {
                                        Map<String, Object> m = new HashMap<>();
                                        m.put("name", e.getKey());
                                        m.put("value", e.getValue());
                                        return m;
                                })
                                .collect(java.util.stream.Collectors.toList());
        }
}
