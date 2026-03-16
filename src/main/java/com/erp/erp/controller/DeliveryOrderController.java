package com.erp.erp.controller;

import com.erp.erp.dto.sales.DeliveryOrderRequest;
import com.erp.erp.dto.sales.DeliveryOrderResponse;
import com.erp.erp.service.DeliveryOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/delivery-orders")
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    @PostMapping
    public ResponseEntity<DeliveryOrderResponse> createDeliveryOrder(@RequestBody DeliveryOrderRequest request) {
        return new ResponseEntity<>(deliveryOrderService.createDeliveryOrder(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryOrderResponse>> getAllDeliveryOrders() {
        return ResponseEntity.ok(deliveryOrderService.getAllDeliveryOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryOrderResponse> getDeliveryOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryOrderService.getDeliveryOrderById(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<DeliveryOrderResponse> approveDeliveryOrder(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryOrderService.approveDeliveryOrder(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<DeliveryOrderResponse> markAsShipped(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryOrderService.markAsShipped(id));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<DeliveryOrderResponse> markAsDelivered(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryOrderService.markAsDelivered(id));
    }
}
