package com.erp.erp.service;

import com.erp.erp.dto.manufacturing.WorkOrderRequest;
import com.erp.erp.dto.manufacturing.WorkOrderResponse;
import com.erp.erp.model.*;
import com.erp.erp.repository.BomRepository;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.repository.WarehouseRepository;
import com.erp.erp.repository.WorkOrderRepository;
import com.erp.erp.util.CodeGeneratorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final BomRepository bomRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public WorkOrderService(WorkOrderRepository workOrderRepository, BomRepository bomRepository,
            ProductRepository productRepository, WarehouseRepository warehouseRepository,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.workOrderRepository = workOrderRepository;
        this.bomRepository = bomRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public WorkOrderResponse createWorkOrder(WorkOrderRequest request) {
        Bom bom = null;
        if (request.getBomId() != null) {
            bom = bomRepository.findById(request.getBomId())
                    .orElseThrow(() -> new RuntimeException("BOM not found"));
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        WorkOrder workOrder = new WorkOrder();
        workOrder.setCode(codeGeneratorUtil.generateCode("WO"));
        workOrder.setBom(bom);
        workOrder.setProduct(product);
        workOrder.setWarehouse(warehouse);
        workOrder.setPlannedQuantity(request.getPlannedQuantity());
        workOrder.setStartDate(request.getStartDate());
        workOrder.setEndDate(request.getEndDate());
        workOrder.setNote(request.getNote());
        workOrder.setStatus(WorkOrder.Status.PLANNED);

        // Map items
        List<WorkOrderItem> items = request.getItems().stream().map(itemReq -> {
            Product material = productRepository.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            WorkOrderItem item = new WorkOrderItem();
            item.setWorkOrder(workOrder);
            item.setMaterial(material);
            item.setPlannedQuantity(itemReq.getPlannedQuantity());
            item.setNote(itemReq.getNote());
            return item;
        }).collect(Collectors.toList());

        workOrder.setItems(items);
        WorkOrder saved = workOrderRepository.save(workOrder);
        return mapToResponse(saved);
    }

    public Page<WorkOrderResponse> getAllWorkOrders(Pageable pageable) {
        return workOrderRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public WorkOrderResponse getWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("WorkOrder not found"));
    }

    public WorkOrderResponse updateStatus(Long id, WorkOrder.Status status) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkOrder not found"));
        workOrder.setStatus(status);
        return mapToResponse(workOrderRepository.save(workOrder));
    }

    public void deleteWorkOrder(Long id) {
        workOrderRepository.deleteById(id);
    }

    private WorkOrderResponse mapToResponse(WorkOrder wo) {
        return WorkOrderResponse.builder()
                .id(wo.getId())
                .code(wo.getCode())
                .bomId(wo.getBom() != null ? wo.getBom().getId() : null)
                .bomName(wo.getBom() != null ? wo.getBom().getName() : null)
                .productId(wo.getProduct().getId())
                .productName(wo.getProduct().getName())
                .warehouseId(wo.getWarehouse().getId())
                .warehouseName(wo.getWarehouse().getName())
                .plannedQuantity(wo.getPlannedQuantity())
                .producedQuantity(wo.getProducedQuantity())
                .startDate(wo.getStartDate())
                .endDate(wo.getEndDate())
                .status(wo.getStatus().name())
                .note(wo.getNote())
                .createdDate(wo.getCreatedDate())
                .items(wo.getItems().stream().map(item -> WorkOrderResponse.WorkOrderItemResponse.builder()
                        .id(item.getId())
                        .materialId(item.getMaterial().getId())
                        .materialName(item.getMaterial().getName())
                        .plannedQuantity(item.getPlannedQuantity())
                        .consumedQuantity(item.getConsumedQuantity())
                        .note(item.getNote())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
