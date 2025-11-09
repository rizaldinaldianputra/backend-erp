package com.erp.erp.workflow.listener;

import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.repository.PurchaseRequestRepository;
import com.erp.erp.workflow.rules.PRStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component("RequisitionStatusListener")
public class RequisitionStatusListener implements ExecutionListener {

    private final PurchaseRequestRepository purchaseRequestRepository;

    public RequisitionStatusListener(PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // ambil status dari BPMN input parameter
        String newStatus = (String) execution.getVariable("REQUISITION_STATUS");
        String documentNumber = (String) execution.getVariable("documentNumber");

        if (documentNumber == null) {
            System.out.println("⚠️ documentNumber tidak ditemukan di variable process!");
            return;
        }

        PurchaseRequest pr = purchaseRequestRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("PurchaseRequest not found: " + documentNumber));

        if (newStatus != null) {
            pr.setStatus(PRStatus.valueOf(newStatus));
            purchaseRequestRepository.save(pr);
            System.out.printf("✅ [%s] status updated to %s%n", documentNumber, newStatus);
        }
    }
}
