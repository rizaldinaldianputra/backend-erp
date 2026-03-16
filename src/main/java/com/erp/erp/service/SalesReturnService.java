package com.erp.erp.service;

import com.erp.erp.dto.sales.SalesReturnRequest;
import com.erp.erp.dto.sales.SalesReturnResponse;
import com.erp.erp.model.*;
import com.erp.erp.repository.*;
import com.erp.erp.util.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesReturnService {

        private final SalesReturnRepository salesReturnRepository;
        private final SalesOrderRepository salesOrderRepository;
        private final DeliveryOrderRepository deliveryOrderRepository;
        private final CustomerRepository customerRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final CodeGeneratorUtil codeGeneratorUtil;

        @Transactional
        public SalesReturnResponse createSalesReturn(SalesReturnRequest request) {
                Customer customer = customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                SalesOrder salesOrder = request.getSalesOrderId() != null
                                ? salesOrderRepository.findById(request.getSalesOrderId()).orElse(null)
                                : null;

                DeliveryOrder deliveryOrder = request.getDeliveryOrderId() != null
                                ? deliveryOrderRepository.findById(request.getDeliveryOrderId()).orElse(null)
                                : null;

                String code = codeGeneratorUtil.generateCode("SR");

                SalesReturn salesReturn = SalesReturn.builder()
                                .code(code)
                                .salesOrder(salesOrder)
                                .deliveryOrder(deliveryOrder)
                                .customer(customer)
                                .warehouse(warehouse)
                                .returnDate(request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now())
                                .note(request.getNote())
                                .status(SalesReturn.Status.DRAFT)
                                .totalAmount(BigDecimal.ZERO)
                                .build();

                BigDecimal[] totalAmount = { BigDecimal.ZERO };

                List<SalesReturnItem> items = request.getItems().stream().map(itemRequest -> {
                        Product product = productRepository.findById(itemRequest.getProductId())
                                        .orElseThrow(() -> new RuntimeException("Product not found"));

                        // Calculate subtotal based on product target price (simplified)
                        BigDecimal subTotal = product.getCostPrice()
                                        .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                        totalAmount[0] = totalAmount[0].add(subTotal);

                        return SalesReturnItem.builder()
                                        .salesReturn(salesReturn)
                                        .product(product)
                                        .quantity(itemRequest.getQuantity())
                                        .unitPrice(product.getCostPrice())
                                        .subTotal(subTotal)
                                        .reason(itemRequest.getReason())
                                        .condition(itemRequest.getCondition())
                                        .build();
                }).collect(Collectors.toList());

                salesReturn.setItems(items);
                salesReturn.setTotalAmount(totalAmount[0]);

                SalesReturn savedSR = salesReturnRepository.save(salesReturn);
                return mapToResponse(savedSR);
        }

        @Transactional(readOnly = true)
        public List<SalesReturnResponse> getAllSalesReturns() {
                return salesReturnRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public SalesReturnResponse getSalesReturnById(Long id) {
                SalesReturn salesReturn = salesReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sales Return not found"));
                return mapToResponse(salesReturn);
        }

        @Transactional
        public SalesReturnResponse approveSalesReturn(Long id) {
                SalesReturn salesReturn = salesReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sales Return not found"));

                salesReturn.setStatus(SalesReturn.Status.APPROVED);
                SalesReturn updatedSR = salesReturnRepository.save(salesReturn);
                return mapToResponse(updatedSR);
        }

        @Transactional
        public SalesReturnResponse rejectSalesReturn(Long id) {
                SalesReturn salesReturn = salesReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sales Return not found"));

                salesReturn.setStatus(SalesReturn.Status.REJECTED);
                SalesReturn updatedSR = salesReturnRepository.save(salesReturn);
                return mapToResponse(updatedSR);
        }

        @Transactional
        public SalesReturnResponse processSalesReturn(Long id) {
                SalesReturn salesReturn = salesReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Sales Return not found"));

                if (salesReturn.getStatus() != SalesReturn.Status.APPROVED) {
                        throw new RuntimeException("Only APPROVED returns can be processed.");
                }

                // This is where Stock should naturally be incremented again.
                // We'd call an InventoryService method or just mark as processed here.
                salesReturn.setStatus(SalesReturn.Status.PROCESSED);
                SalesReturn updatedSR = salesReturnRepository.save(salesReturn);
                return mapToResponse(updatedSR);
        }

        private SalesReturnResponse mapToResponse(SalesReturn salesReturn) {
                return SalesReturnResponse.builder()
                                .id(salesReturn.getId())
                                .code(salesReturn.getCode())
                                .salesOrderId(salesReturn.getSalesOrder() != null ? salesReturn.getSalesOrder().getId()
                                                : null)
                                .salesOrderCode(salesReturn.getSalesOrder() != null
                                                ? salesReturn.getSalesOrder().getCode()
                                                : null)
                                .deliveryOrderId(salesReturn.getDeliveryOrder() != null
                                                ? salesReturn.getDeliveryOrder().getId()
                                                : null)
                                .deliveryOrderCode(
                                                salesReturn.getDeliveryOrder() != null
                                                                ? salesReturn.getDeliveryOrder().getCode()
                                                                : null)
                                .customerId(salesReturn.getCustomer().getId())
                                .customerName(salesReturn.getCustomer().getName())
                                .warehouseId(salesReturn.getWarehouse().getId())
                                .warehouseName(salesReturn.getWarehouse().getName())
                                .returnDate(salesReturn.getReturnDate())
                                .note(salesReturn.getNote())
                                .totalAmount(salesReturn.getTotalAmount())
                                .status(salesReturn.getStatus().name())
                                .items(salesReturn.getItems().stream()
                                                .map(item -> SalesReturnResponse.SalesReturnItemResponse.builder()
                                                                .id(item.getId())
                                                                .productId(item.getProduct().getId())
                                                                .productName(item.getProduct().getName())
                                                                .quantity(item.getQuantity())
                                                                .unitPrice(item.getUnitPrice())
                                                                .subTotal(item.getSubTotal())
                                                                .reason(item.getReason())
                                                                .condition(item.getCondition())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();
        }
}
