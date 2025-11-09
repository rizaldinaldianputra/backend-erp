package com.erp.erp.service;

import com.erp.erp.dto.PurchaseRequestItemResponse;
import com.erp.erp.dto.PurchaseRequestResponse;
import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.model.User;
import com.erp.erp.repository.PurchaseRequestRepository;
import com.erp.erp.workflow.engine.WorkflowService;
import com.erp.erp.workflow.rules.PRStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository prRepository;
    private final WorkflowService workflowService;

    public PurchaseRequestService(PurchaseRequestRepository prRepository, WorkflowService workflowService) {
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
        // Generate document number unik
        String docNum = "PR/" + System.currentTimeMillis();
        pr.setDocumentNumber(docNum);
        pr.setStatus(PRStatus.DRAFT); // awal status

        // Save PR
        PurchaseRequest saved = prRepository.save(pr);

        // Ambil supervisor/atasan creator
        User creator = saved.getCreatedBy();
        User approver = creator.getSupervisor();
        if (approver == null) {
            throw new RuntimeException("Creator " + creator.getUsername() + " has no supervisor assigned");
        }

        // ===== Mulai BPMN process otomatis =====
        Map<String, Object> vars = Map.of(
                "documentNumber", saved.getDocumentNumber(),
                "CREATOR", creator.getUsername(),
                "APPROVER", approver.getUsername(),
                "REQUISITION_STATUS", "DRAFT");
        workflowService.startProcess("start_requisition_process", vars);
        // ======================================

        return saved;
    }

    @Transactional
    public PurchaseRequest submit(Long id) {
        PurchaseRequest pr = getPRById(id);

        // Ubah status via workflow
        workflowService.changeStatus(pr, PRStatus.SUBMITTED);

        return prRepository.save(pr);
    }

    @Transactional
    public PurchaseRequest approve(Long id) {
        PurchaseRequest pr = getPRById(id);

        workflowService.changeStatus(pr, PRStatus.APPROVED);

        return prRepository.save(pr);
    }

    @Transactional
    public PurchaseRequest reject(Long id) {
        PurchaseRequest pr = getPRById(id);

        workflowService.changeStatus(pr, PRStatus.DRAFT);

        return prRepository.save(pr);
    }

    private PurchaseRequest getPRById(Long id) {
        return prRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found with id " + id));
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
                .createdBy(pr.getCreatedBy() != null ? pr.getCreatedBy().getUsername() : null)
                .status(pr.getStatus().name())
                .items(items)
                .build();
    }
}
