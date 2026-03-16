package com.erp.erp.workflow.delegate;

import com.erp.erp.workflow.service.DocumentStatusService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Called by Camunda after approver clicks Approve.
 * Sets document status to the appropriate approved state per documentType.
 */
@Component("approveDocumentDelegate")
public class ApproveDocumentDelegate implements JavaDelegate {

    @Autowired
    private DocumentStatusService documentStatusService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String documentType = (String) execution.getVariable("documentType");
        Long documentId = Long.valueOf(execution.getVariable("documentId").toString());
        String approvedStatus = documentStatusService.getApprovedStatus(documentType);
        documentStatusService.setStatus(documentType, documentId, approvedStatus);
    }
}
