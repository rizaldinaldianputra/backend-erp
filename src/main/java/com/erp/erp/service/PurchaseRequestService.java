package com.erp.erp.service;

import com.erp.erp.dto.PurchaseRequestItemResponse;
import com.erp.erp.dto.PurchaseRequestResponse;
import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.model.User;
import com.erp.erp.repository.PurchaseRequestRepository;
import com.erp.erp.security.SecurityUtil;
import com.erp.erp.workflow.engine.WorkflowTaskService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository prRepository;
    private final WorkflowTaskService workflowService;

    public PurchaseRequestService(PurchaseRequestRepository prRepository, WorkflowTaskService workflowService) {
        this.prRepository = prRepository;
        this.workflowService = workflowService;
    }

    public List<PurchaseRequestResponse> findAll() {
        return prRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PurchaseRequestResponse findById(Long id) {
        return prRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found"));
    }

    @Transactional
    public PurchaseRequest create(PurchaseRequest pr) {
        // Ambil user login dari token
        User creator = SecurityUtil.getCurrentUser();
        if (creator == null) {
            throw new RuntimeException("User not authenticated or not found");
        }

        // Set creator ke PR (username)
        pr.setCreatedBy(creator.getUsername());

        // Generate nomor dokumen
        String docNum = "PR/" + System.currentTimeMillis();
        pr.setDocumentNumber(docNum);
        pr.setStatus("DRAFT");

        // Set relation untuk PR items
        if (pr.getItems() != null) {
            pr.getItems().forEach(item -> item.setPurchaseRequest(pr));
        }

        // Simpan PR
        PurchaseRequest saved = prRepository.save(pr);

        // Ambil supervisor
        User approver = creator.getSupervisor();
        if (approver == null) {
            throw new RuntimeException("Creator " + creator.getUsername() + " has no supervisor assigned");
        }

        // Jalankan workflow BPMN
        Map<String, Object> vars = Map.of(
                "documentNumber", saved.getDocumentNumber(),
                "CREATOR", creator.getUsername(),
                "APPROVER", approver.getUsername(),
                "REQUISITION_STATUS", "DRAFT");
        workflowService.startProcess("requisition_process", vars);

        return saved;
    }

    private PurchaseRequestResponse mapToResponse(PurchaseRequest pr) {
        List<PurchaseRequestItemResponse> items = pr.getItems().stream()
                .map(item -> PurchaseRequestItemResponse.builder()
                        .id(item.getId())
                        .type(item.getType())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .serviceId(item.getServiceId())
                        .serviceName(item.getServiceName())
                        .quantity(item.getQuantity())
                        .estimatedPrice(item.getEstimatedPrice())
                        .reason(item.getReason())
                        .build())
                .collect(Collectors.toList());

        return PurchaseRequestResponse.builder()
                .id(pr.getId())
                .documentNumber(pr.getDocumentNumber())
                .trxDate(pr.getTrxDate())
                .requiredDate(pr.getRequiredDate())
                .notes(pr.getNotes())
                .createdBy(pr.getCreatedBy() != null ? pr.getCreatedBy() : null)
                .status(pr.getStatus())
                .items(items)
                .build();
    }
}
