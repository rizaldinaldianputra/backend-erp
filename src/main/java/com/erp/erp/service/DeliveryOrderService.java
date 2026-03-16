package com.erp.erp.service;

import com.erp.erp.dto.sales.DeliveryOrderRequest;
import com.erp.erp.dto.sales.DeliveryOrderResponse;
import com.erp.erp.model.*;
import com.erp.erp.repository.*;
import com.erp.erp.util.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryOrderService {

        private final DeliveryOrderRepository deliveryOrderRepository;
        private final SalesOrderRepository salesOrderRepository;
        private final CustomerRepository customerRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final CodeGeneratorUtil codeGeneratorUtil;
        private final AccountingService accountingService;

        @Transactional
        public DeliveryOrderResponse createDeliveryOrder(DeliveryOrderRequest request) {
                SalesOrder salesOrder = salesOrderRepository.findById(request.getSalesOrderId())
                                .orElseThrow(() -> new RuntimeException("Sales Order not found"));

                Customer customer = customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                String code = codeGeneratorUtil.generateCode("DO");

                DeliveryOrder deliveryOrder = DeliveryOrder.builder()
                                .code(code)
                                .salesOrder(salesOrder)
                                .customer(customer)
                                .warehouse(warehouse)
                                .deliveryDate(request.getDeliveryDate() != null ? request.getDeliveryDate()
                                                : LocalDate.now())
                                .note(request.getNote())
                                .status(DeliveryOrder.Status.DRAFT)
                                .build();

                List<DeliveryOrderItem> items = request.getItems().stream().map(itemRequest -> {
                        Product product = productRepository.findById(itemRequest.getProductId())
                                        .orElseThrow(() -> new RuntimeException("Product not found"));

                        return DeliveryOrderItem.builder()
                                        .deliveryOrder(deliveryOrder)
                                        .product(product)
                                        .quantity(itemRequest.getQuantity())
                                        .deliveredQuantity(0)
                                        .note(itemRequest.getNote())
                                        .build();
                }).collect(Collectors.toList());

                deliveryOrder.setItems(items);

                DeliveryOrder savedDO = deliveryOrderRepository.save(deliveryOrder);
                return mapToResponse(savedDO);
        }

        @Transactional(readOnly = true)
        public List<DeliveryOrderResponse> getAllDeliveryOrders() {
                return deliveryOrderRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public DeliveryOrderResponse getDeliveryOrderById(Long id) {
                DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Delivery Order not found"));
                return mapToResponse(deliveryOrder);
        }

        @Transactional
        public DeliveryOrderResponse approveDeliveryOrder(Long id) {
                DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Delivery Order not found"));

                if (deliveryOrder.getStatus() != DeliveryOrder.Status.DRAFT) {
                        throw new RuntimeException("Only DRAFT DO can be approved");
                }

                // Normally handled via Camunda, but adding a direct method for simplicity
                deliveryOrder.setStatus(DeliveryOrder.Status.PENDING_APPROVAL);
                DeliveryOrder updatedDO = deliveryOrderRepository.save(deliveryOrder);
                return mapToResponse(updatedDO);
        }

        @Transactional
        public DeliveryOrderResponse markAsShipped(Long id) {
                DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Delivery Order not found"));

                deliveryOrder.setStatus(DeliveryOrder.Status.SHIPPED);
                DeliveryOrder updatedDO = deliveryOrderRepository.save(deliveryOrder);

                // Trigger Journal Creation for DO
                accountingService.createJournalFromDeliveryOrder(updatedDO);

                return mapToResponse(updatedDO);
        }

        @Transactional
        public DeliveryOrderResponse markAsDelivered(Long id) {
                DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Delivery Order not found"));

                deliveryOrder.setStatus(DeliveryOrder.Status.DELIVERED);

                // Update Actual Delivered Quantity to match requested if not specified manually
                deliveryOrder.getItems().forEach(item -> {
                        if (item.getDeliveredQuantity() == null || item.getDeliveredQuantity() == 0) {
                                item.setDeliveredQuantity(item.getQuantity());
                        }
                });

                DeliveryOrder updatedDO = deliveryOrderRepository.save(deliveryOrder);
                return mapToResponse(updatedDO);
        }

        private DeliveryOrderResponse mapToResponse(DeliveryOrder deliveryOrder) {
                return DeliveryOrderResponse.builder()
                                .id(deliveryOrder.getId())
                                .code(deliveryOrder.getCode())
                                .salesOrderId(deliveryOrder.getSalesOrder() != null
                                                ? deliveryOrder.getSalesOrder().getId()
                                                : null)
                                .salesOrderCode(deliveryOrder.getSalesOrder() != null
                                                ? deliveryOrder.getSalesOrder().getCode()
                                                : null)
                                .customerId(deliveryOrder.getCustomer().getId())
                                .customerName(deliveryOrder.getCustomer().getName())
                                .warehouseId(deliveryOrder.getWarehouse() != null ? deliveryOrder.getWarehouse().getId()
                                                : null)
                                .warehouseName(deliveryOrder.getWarehouse() != null
                                                ? deliveryOrder.getWarehouse().getName()
                                                : null)
                                .deliveryDate(deliveryOrder.getDeliveryDate())
                                .note(deliveryOrder.getNote())
                                .status(deliveryOrder.getStatus().name())
                                .items(deliveryOrder.getItems().stream()
                                                .map(item -> DeliveryOrderResponse.DeliveryOrderItemResponse.builder()
                                                                .id(item.getId())
                                                                .productId(item.getProduct().getId())
                                                                .productName(item.getProduct().getName())
                                                                .quantity(item.getQuantity())
                                                                .deliveredQuantity(item.getDeliveredQuantity())
                                                                .note(item.getNote())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();
        }
}
