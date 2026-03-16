package com.erp.erp.workflow.delegate;

import com.erp.erp.workflow.service.DocumentStatusService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("rejectDocumentDelegate")
public class RejectDocumentDelegate implements JavaDelegate {

    @Autowired
    private DocumentStatusService documentStatusService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String documentType = (String) execution.getVariable("documentType");
        Long documentId = Long.valueOf(execution.getVariable("documentId").toString());
        String rejectedStatus = getRejectedStatus(documentType);
        documentStatusService.setStatus(documentType, documentId, rejectedStatus);
    }

    private String getRejectedStatus(String documentType) {
        return switch (documentType) {
            case "SALES_ORDER", "INVOICE", "DELIVERY_ORDER" -> "CANCELLED";
            case "SALES_RETURN", "PURCHASE_RETURN", "BUDGET" -> "REJECTED";
            default -> "REJECTED";
        };
    }
}
