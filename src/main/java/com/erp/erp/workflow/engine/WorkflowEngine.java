// src/main/java/com/erp/erp/service/WorkflowEngine.java
package com.erp.erp.workflow.engine;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkflowEngine {

    private final RuntimeService runtimeService;

    public WorkflowEngine(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void startProcess(String processKey, Map<String, Object> variables) {
        runtimeService.startProcessInstanceByKey(processKey, variables);
    }
}
