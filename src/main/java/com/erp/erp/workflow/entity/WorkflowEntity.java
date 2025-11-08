package com.erp.erp.workflow.entity;

import com.erp.erp.workflow.rules.Status;

public interface WorkflowEntity {
    Status getStatus();

    void setStatus(Status status);

    String getModule();
}
