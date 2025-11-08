package com.erp.erp.workflow.engine;

import com.erp.erp.workflow.entity.WorkflowEntity;
import com.erp.erp.workflow.rules.Status;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowService {

    private final WorkflowEngine workflowEngine;

    public WorkflowService(WorkflowEngine workflowEngine) {
        this.workflowEngine = workflowEngine;
    }

    @Transactional
    public <T extends WorkflowEntity> T changeStatus(T entity, Status nextStatus) {
        Status currentStatus = entity.getStatus();
        String module = entity.getModule();

        if (!workflowEngine.canTransition(module, currentStatus, nextStatus)) {
            throw new RuntimeException(
                    "Transition not allowed: " + currentStatus.name() + " -> " + nextStatus.name() + " for module "
                            + module);
        }

        entity.setStatus(nextStatus);
        return entity;
    }
}
