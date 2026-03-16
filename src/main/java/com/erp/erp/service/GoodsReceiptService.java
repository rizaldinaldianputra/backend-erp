package com.erp.erp.service;

import com.erp.erp.dto.purchase.GoodsReceiptRequest;
import com.erp.erp.model.*;
import com.erp.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.erp.erp.util.CodeGeneratorUtil;

@Service
@Transactional
public class GoodsReceiptService {

    private final GoodsReceiptRepository grRepository;
    private final PurchaseOrderRepository poRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final CodeGeneratorUtil codeGeneratorUtil;
    private final AccountingService accountingService;

    public GoodsReceiptService(
            GoodsReceiptRepository grRepository,
            PurchaseOrderRepository poRepository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository,
            InventoryService inventoryService,
            CodeGeneratorUtil codeGeneratorUtil,
            AccountingService accountingService) {
        this.grRepository = grRepository;
        this.poRepository = poRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.codeGeneratorUtil = codeGeneratorUtil;
        this.accountingService = accountingService;
    }

    public GoodsReceipt createGoodsReceipt(GoodsReceiptRequest request) {
        PurchaseOrder po = poRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        GoodsReceipt gr = new GoodsReceipt();
        // Update to use documentNumber and CodeGeneratorUtil
        if (gr.getCode() == null || gr.getCode().isEmpty()) {
            gr.setCode(codeGeneratorUtil.generateCode("GR"));
        }
        gr.setPurchaseOrder(po);
        gr.setWarehouse(warehouse);
        gr.setDate(request.getDate());
        gr.setNote(request.getNote());

        List<GoodsReceiptItem> items = new ArrayList<>();

        for (GoodsReceiptRequest.GoodsReceiptItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            GoodsReceiptItem item = new GoodsReceiptItem();
            item.setGoodsReceipt(gr);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            items.add(item);

            // Update Inventory
            inventoryService.increaseStock(
                    product.getId(),
                    warehouse.getId(),
                    itemReq.getQuantity(),
                    gr.getCode(),
                    "Goods Receipt from PO: " + po.getDocumentNumber());
        }

        gr.setItems(items);
        GoodsReceipt savedGR = grRepository.save(gr);

        // Trigger Journal for Goods Receipt
        accountingService.createJournalFromGoodsReceipt(savedGR);

        return savedGR;
    }

    public List<GoodsReceipt> getAllGoodsReceipts() {
        return grRepository.findAll();
    }

    public Optional<GoodsReceipt> getGoodsReceiptById(Long id) {
        return grRepository.findById(id);
    }
}
