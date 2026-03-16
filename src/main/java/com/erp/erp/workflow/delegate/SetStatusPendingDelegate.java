package com.erp.erp.workflow.delegate;

import com.erp.erp.workflow.service.DocumentStatusService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Called by Camunda when document is submitted — sets status to
 * PENDING_APPROVAL.
 */
@Component("setStatusPendingDelegate")
public class SetStatusPendingDelegate implements JavaDelegate {

    @Autowired
    private DocumentStatusService documentStatusService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String documentType = (String) execution.getVariable("documentType");
        Long documentId = Long.valueOf(execution.getVariable("documentId").toString());
        documentStatusService.setStatus(documentType, documentId, "PENDING_APPROVAL");
    }
}
