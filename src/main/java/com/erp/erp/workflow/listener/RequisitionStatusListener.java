// src/main/java/com/erp/erp/workflow/listener/RequisitionStatusListener.java
package com.erp.erp.workflow.listener;

import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.repository.PurchaseRequestRepository;
import com.erp.erp.workflow.rules.PRStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component("RequisitionStatusListener")
public class RequisitionStatusListener implements ExecutionListener {

    private final PurchaseRequestRepository prRepository;

    public RequisitionStatusListener(PurchaseRequestRepository prRepository) {
        this.prRepository = prRepository;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String newStatus = (String) execution.getVariable("REQUISITION_STATUS");
        String documentNumber = (String) execution.getVariable("documentNumber");
        if (documentNumber == null)
            return;

        PurchaseRequest pr = prRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("PurchaseRequest not found: " + documentNumber));

        if (newStatus != null) {
            try {
                PRStatus st = PRStatus.valueOf(newStatus);
                pr.setStatus(st);
                prRepository.save(pr);
                System.out.printf("RequisitionStatusListener: updated %s -> %s%n", documentNumber, newStatus);
            } catch (IllegalArgumentException e) {
                // ignore unknown statuses
                System.out.printf("Unknown PR status: %s%n", newStatus);
            }
        }
    }
}
