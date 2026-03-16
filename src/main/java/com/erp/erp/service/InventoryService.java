package com.erp.erp.service;

import com.erp.erp.dto.inventory.StockAdjustmentRequest;
import com.erp.erp.dto.inventory.StockTransferRequest;
import com.erp.erp.model.Inventory;
import com.erp.erp.model.Product;
import com.erp.erp.model.StockMovement;
import com.erp.erp.model.Warehouse;
import com.erp.erp.repository.InventoryRepository;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.repository.StockMovementRepository;
import com.erp.erp.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getWarehouse().getId().equals(warehouseId))
                .toList();
    }

    public Inventory getInventoryByProductAndWarehouse(Long productId, Long warehouseId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        return inventoryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseGet(() -> {
                    Inventory newInv = new Inventory();
                    newInv.setProduct(product);
                    newInv.setWarehouse(warehouse);
                    newInv.setQuantity(0);
                    return inventoryRepository.save(newInv);
                });
    }

    public void increaseStock(Long productId, Long warehouseId, Integer quantity, String reference,
            String description) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseId);
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);

        StockMovement movement = new StockMovement();
        movement.setProduct(inventory.getProduct());
        movement.setWarehouse(inventory.getWarehouse());
        movement.setType(StockMovement.MovementType.IN);
        movement.setQuantity(quantity);
        movement.setReferenceCode(reference);
        movement.setDescription(description);
        stockMovementRepository.save(movement);
    }

    public void decreaseStock(Long productId, Long warehouseId, Integer quantity, String reference,
            String description) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseId);
        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product " + inventory.getProduct().getCode());
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        StockMovement movement = new StockMovement();
        movement.setProduct(inventory.getProduct());
        movement.setWarehouse(inventory.getWarehouse());
        movement.setType(StockMovement.MovementType.OUT);
        movement.setQuantity(-quantity); // Negative for OUT
        movement.setReferenceCode(reference);
        movement.setDescription(description);
        stockMovementRepository.save(movement);
    }

    public StockMovement adjustStock(StockAdjustmentRequest request) {
        Inventory inventory = getInventoryByProductAndWarehouse(request.getProductId(), request.getWarehouseId());

        int quantityDiff;
        StockMovement.MovementType type;

        if ("OPNAME".equalsIgnoreCase(request.getType())) {
            // Opname: Set to actual quantity
            if (request.getActualQuantity() == null || request.getActualQuantity() < 0) {
                throw new IllegalArgumentException("Actual quantity required for Opname");
            }
            quantityDiff = request.getActualQuantity() - inventory.getQuantity();
            type = StockMovement.MovementType.ADJUSTMENT;
        } else {
            // Adjustment: Add/Subtract adjustment quantity
            if (request.getAdjustmentQuantity() == null) {
                throw new IllegalArgumentException("Adjustment quantity required");
            }
            quantityDiff = request.getAdjustmentQuantity();
            type = StockMovement.MovementType.ADJUSTMENT;
        }

        inventory.setQuantity(inventory.getQuantity() + quantityDiff);
        if (inventory.getQuantity() < 0)
            throw new RuntimeException("Resulting stock cannot be negative");

        inventoryRepository.save(inventory);

        StockMovement movement = new StockMovement();
        movement.setProduct(inventory.getProduct());
        movement.setWarehouse(inventory.getWarehouse());
        movement.setType(type);
        movement.setQuantity(quantityDiff);
        movement.setReferenceCode("ADJ-" + System.currentTimeMillis());
        movement.setDescription(request.getDescription());

        return stockMovementRepository.save(movement);
    }

    public StockMovement transferStock(StockTransferRequest request) {
        if (request.getQuantity() <= 0)
            throw new IllegalArgumentException("Transfer quantity must be positive");
        if (request.getFromWarehouseId().equals(request.getToWarehouseId())) {
            throw new IllegalArgumentException("Cannot transfer to same warehouse");
        }

        // 1. Decrease from Source
        decreaseStock(request.getProductId(), request.getFromWarehouseId(), request.getQuantity(),
                "TRF-OUT-" + System.currentTimeMillis(), "Transfer to " + request.getToWarehouseId());

        // 2. Increase to Destination
        increaseStock(request.getProductId(), request.getToWarehouseId(), request.getQuantity(),
                "TRF-IN-" + System.currentTimeMillis(), "Transfer from " + request.getFromWarehouseId());

        return null;
    }
}
