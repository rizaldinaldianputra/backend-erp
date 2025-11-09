package com.erp.erp.workflow.engine;

import com.erp.erp.workflow.entity.WorkflowEntity;
import com.erp.erp.workflow.rules.Status;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class WorkflowService {

    private final WorkflowEngine workflowEngine;
    private final RuntimeService runtimeService; // tambah ini

    public WorkflowService(WorkflowEngine workflowEngine, RuntimeService runtimeService) {
        this.workflowEngine = workflowEngine;
        this.runtimeService = runtimeService;
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

    // ===== Tambahkan method untuk start BPMN process =====
    @Transactional
    public void startProcess(String processKey, Map<String, Object> variables) {
        runtimeService.startProcessInstanceByKey(processKey, variables);
    }
}
