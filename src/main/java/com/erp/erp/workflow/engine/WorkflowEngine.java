package com.erp.erp.workflow.engine;

import com.erp.erp.workflow.rules.Status;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WorkflowEngine {

    // Contoh konfigurasi sederhana: module -> currentStatus -> allowed next
    // statuses
    private final Map<String, Map<Status, List<Status>>> rules = new HashMap<>();

    public WorkflowEngine() {
        Map<Status, List<Status>> prRules = new HashMap<>();
        prRules.put(com.erp.erp.workflow.rules.PRStatus.DRAFT, Arrays.asList(
                com.erp.erp.workflow.rules.PRStatus.ASSIGN,
                com.erp.erp.workflow.rules.PRStatus.SUBMITTED));
        prRules.put(com.erp.erp.workflow.rules.PRStatus.ASSIGN, Collections.singletonList(
                com.erp.erp.workflow.rules.PRStatus.SUBMITTED));
        prRules.put(com.erp.erp.workflow.rules.PRStatus.SUBMITTED, Arrays.asList(
                com.erp.erp.workflow.rules.PRStatus.APPROVED,
                com.erp.erp.workflow.rules.PRStatus.REJECTED));
        prRules.put(com.erp.erp.workflow.rules.PRStatus.REJECTED, Collections.singletonList(
                com.erp.erp.workflow.rules.PRStatus.DRAFT));
        prRules.put(com.erp.erp.workflow.rules.PRStatus.APPROVED, Collections.emptyList());

        rules.put("PR", prRules);
    }

    public boolean canTransition(String module, Status current, Status next) {
        Map<Status, List<Status>> moduleRules = rules.get(module);
        if (moduleRules == null)
            return false;
        return moduleRules.getOrDefault(current, Collections.emptyList()).contains(next);
    }
}
