package com.erp.erp.service;

import com.erp.erp.dto.purchase.PurchaseReturnRequest;
import com.erp.erp.dto.purchase.PurchaseReturnResponse;
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
public class PurchaseReturnService {

        private final PurchaseReturnRepository purchaseReturnRepository;
        private final PurchaseOrderRepository purchaseOrderRepository;
        private final GoodsReceiptRepository goodsReceiptRepository;
        private final SupplierRepository supplierRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final CodeGeneratorUtil codeGeneratorUtil;

        @Transactional
        public PurchaseReturnResponse createPurchaseReturn(PurchaseReturnRequest request) {
                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                PurchaseOrder purchaseOrder = request.getPurchaseOrderId() != null
                                ? purchaseOrderRepository.findById(request.getPurchaseOrderId()).orElse(null)
                                : null;

                GoodsReceipt goodsReceipt = request.getGoodsReceiptId() != null
                                ? goodsReceiptRepository.findById(request.getGoodsReceiptId()).orElse(null)
                                : null;

                String code = codeGeneratorUtil.generateCode("PR");

                PurchaseReturn purchaseReturn = PurchaseReturn.builder()
                                .code(code)
                                .purchaseOrder(purchaseOrder)
                                .goodsReceipt(goodsReceipt)
                                .supplier(supplier)
                                .warehouse(warehouse)
                                .returnDate(request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now())
                                .note(request.getNote())
                                .status(PurchaseReturn.Status.DRAFT)
                                .totalAmount(BigDecimal.ZERO)
                                .build();

                BigDecimal[] totalAmount = { BigDecimal.ZERO };

                List<PurchaseReturnItem> items = request.getItems().stream().map(itemRequest -> {
                        Product product = productRepository.findById(itemRequest.getProductId())
                                        .orElseThrow(() -> new RuntimeException("Product not found"));

                        // Use cost price for purchase returns
                        BigDecimal subTotal = product.getCostPrice()
                                        .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                        totalAmount[0] = totalAmount[0].add(subTotal);

                        return PurchaseReturnItem.builder()
                                        .purchaseReturn(purchaseReturn)
                                        .product(product)
                                        .quantity(itemRequest.getQuantity())
                                        .unitPrice(product.getCostPrice())
                                        .subTotal(subTotal)
                                        .reason(itemRequest.getReason())
                                        .condition(itemRequest.getCondition())
                                        .build();
                }).collect(Collectors.toList());

                purchaseReturn.setItems(items);
                purchaseReturn.setTotalAmount(totalAmount[0]);

                PurchaseReturn savedPR = purchaseReturnRepository.save(purchaseReturn);
                return mapToResponse(savedPR);
        }

        @Transactional(readOnly = true)
        public List<PurchaseReturnResponse> getAllPurchaseReturns() {
                return purchaseReturnRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public PurchaseReturnResponse getPurchaseReturnById(Long id) {
                PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));
                return mapToResponse(purchaseReturn);
        }

        @Transactional
        public PurchaseReturnResponse approvePurchaseReturn(Long id) {
                PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));

                purchaseReturn.setStatus(PurchaseReturn.Status.APPROVED);
                PurchaseReturn updatedPR = purchaseReturnRepository.save(purchaseReturn);
                return mapToResponse(updatedPR);
        }

        @Transactional
        public PurchaseReturnResponse rejectPurchaseReturn(Long id) {
                PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));

                purchaseReturn.setStatus(PurchaseReturn.Status.REJECTED);
                PurchaseReturn updatedPR = purchaseReturnRepository.save(purchaseReturn);
                return mapToResponse(updatedPR);
        }

        @Transactional
        public PurchaseReturnResponse processPurchaseReturn(Long id) {
                PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Return not found"));

                if (purchaseReturn.getStatus() != PurchaseReturn.Status.APPROVED) {
                        throw new RuntimeException("Only APPROVED returns can be processed.");
                }

                purchaseReturn.setStatus(PurchaseReturn.Status.PROCESSED);
                PurchaseReturn updatedPR = purchaseReturnRepository.save(purchaseReturn);
                return mapToResponse(updatedPR);
        }

        private PurchaseReturnResponse mapToResponse(PurchaseReturn purchaseReturn) {
                return PurchaseReturnResponse.builder()
                                .id(purchaseReturn.getId())
                                .code(purchaseReturn.getCode())
                                .purchaseOrderId(
                                                purchaseReturn.getPurchaseOrder() != null
                                                                ? purchaseReturn.getPurchaseOrder().getId()
                                                                : null)
                                .purchaseOrderCode(
                                                purchaseReturn.getPurchaseOrder() != null
                                                                ? purchaseReturn.getPurchaseOrder().getDocumentNumber()
                                                                : null)
                                .goodsReceiptId(
                                                purchaseReturn.getGoodsReceipt() != null
                                                                ? purchaseReturn.getGoodsReceipt().getId()
                                                                : null)
                                .goodsReceiptCode(
                                                purchaseReturn.getGoodsReceipt() != null
                                                                ? purchaseReturn.getGoodsReceipt().getCode()
                                                                : null)
                                .supplierId(purchaseReturn.getSupplier().getId())
                                .supplierName(purchaseReturn.getSupplier().getName())
                                .warehouseId(purchaseReturn.getWarehouse().getId())
                                .warehouseName(purchaseReturn.getWarehouse().getName())
                                .returnDate(purchaseReturn.getReturnDate())
                                .note(purchaseReturn.getNote())
                                .totalAmount(purchaseReturn.getTotalAmount())
                                .status(purchaseReturn.getStatus().name())
                                .items(purchaseReturn.getItems().stream()
                                                .map(item -> PurchaseReturnResponse.PurchaseReturnItemResponse.builder()
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
