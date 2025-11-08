package com.erp.erp.service;

import com.erp.erp.dto.PurchaseRequestItemResponse;
import com.erp.erp.dto.PurchaseRequestResponse;
import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.repository.PurchaseRequestRepository;
import com.erp.erp.workflow.engine.WorkflowService;
import com.erp.erp.workflow.rules.PRStatus;

import org.springframework.stereotype.Service;

import java.util.List;
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

    public PurchaseRequest create(PurchaseRequest pr) {
        String docNum = "PR/" + System.currentTimeMillis();
        pr.setDocumentNumber(docNum);
        return prRepository.save(pr);
    }

    public PurchaseRequest submit(Long id) {
        PurchaseRequest pr = prRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found"));

        workflowService.changeStatus(pr, PRStatus.SUBMITTED);
        return prRepository.save(pr);
    }

    public PurchaseRequest approve(Long id) {
        PurchaseRequest pr = prRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found"));

        workflowService.changeStatus(pr, PRStatus.APPROVED);
        return prRepository.save(pr);
    }

    public PurchaseRequest reject(Long id) {
        PurchaseRequest pr = prRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found"));

        workflowService.changeStatus(pr, PRStatus.DRAFT);
        return prRepository.save(pr);
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
                .createdBy(pr.getCreatedBy().getUsername())
                .status(pr.getStatus().name())
                .items(items)
                .build();
    }
}
