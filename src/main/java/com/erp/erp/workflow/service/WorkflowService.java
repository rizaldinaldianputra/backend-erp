package com.erp.erp.workflow.service;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import com.erp.erp.model.User;
import com.erp.erp.repository.UserRepository;

import java.util.*;

@Service
public class WorkflowService {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final com.erp.erp.repository.ApprovalConfigurationRepository approvalConfigurationRepository;
    private final DocumentStatusService documentStatusService;

    public WorkflowService(RuntimeService runtimeService, TaskService taskService, UserRepository userRepository,
            com.erp.erp.repository.ApprovalConfigurationRepository approvalConfigurationRepository,
            DocumentStatusService documentStatusService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.userRepository = userRepository;
        this.approvalConfigurationRepository = approvalConfigurationRepository;
        this.documentStatusService = documentStatusService;
    }

    /**
     * Start the document-approval process for a given document.
     */
    public String startApproval(String documentType, Long documentId, String documentNumber, String submittedBy) {
        // 1. Check if there's an active approval configuration for this document type
        var configs = approvalConfigurationRepository.findByDocumentTypeAndActiveTrueOrderByMinAmountAsc(documentType);

        if (configs.isEmpty()) {
            // No configuration means auto-approve
            String approvedStatus = documentStatusService.getApprovedStatus(documentType);
            documentStatusService.setStatus(documentType, documentId, approvedStatus);
            return "SKIPPED_NO_CONFIG";
        }

        // 2. Check document amount against config
        java.math.BigDecimal amount = documentStatusService.getDocumentAmount(documentType, documentId);
        boolean needsApproval = false;
        for (var config : configs) {
            if (amount.compareTo(config.getMinAmount()) >= 0) {
                needsApproval = true;
                break;
            }
        }

        if (!needsApproval) {
            String approvedStatus = documentStatusService.getApprovedStatus(documentType);
            documentStatusService.setStatus(documentType, documentId, approvedStatus);
            return "SKIPPED_BELOW_MIN_AMOUNT";
        }

        User creator = userRepository.findByUsername(submittedBy)
                .orElseThrow(() -> new RuntimeException("Submitting user not found"));
        User approver = creator.getSupervisor();
        if (approver == null) {
            throw new RuntimeException("User " + submittedBy + " has no supervisor assigned to approve this document");
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", documentType);
        variables.put("documentId", documentId);
        variables.put("documentNumber", documentNumber);
        variables.put("submittedBy", submittedBy);
        variables.put("supervisorUsername", approver.getUsername());
        variables.put("approved", false); // default

        String businessKey = documentType + "-" + documentId;

        // Check if a process already running for this document
        long count = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("document-approval")
                .processInstanceBusinessKey(businessKey)
                .active()
                .count();
        if (count > 0) {
            throw new RuntimeException("Approval process already running for " + businessKey);
        }

        var instance = runtimeService.startProcessInstanceByKey("document-approval", businessKey, variables);
        return instance.getId();
    }

    /**
     * Get all pending tasks for a specific supervisor (assignee).
     */
    public List<Map<String, Object>> getAllPendingTasks(String username) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime().desc()
                .list();
        return tasks.stream().map(this::mapTask).toList();
    }

    /**
     * Get all tasks submitted by a specific user.
     */
    public List<Map<String, Object>> getMySubmittedTasks(String userId) {
        // Get all active processes submitted by this user
        var instances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("document-approval")
                .variableValueEquals("submittedBy", userId)
                .list();

        return instances.stream().map(pi -> {
            Map<String, Object> info = new HashMap<>();
            info.put("processInstanceId", pi.getId());
            info.put("businessKey", pi.getBusinessKey());
            info.put("documentType", runtimeService.getVariable(pi.getId(), "documentType"));
            info.put("documentId", runtimeService.getVariable(pi.getId(), "documentId"));
            info.put("documentNumber", runtimeService.getVariable(pi.getId(), "documentNumber"));
            info.put("status", "PENDING_APPROVAL");
            return info;
        }).toList();
    }

    /**
     * Complete a review task — approver approves or rejects.
     */
    public void completeTask(String taskId, boolean approved, String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
            throw new RuntimeException("Task not found: " + taskId);

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        variables.put("result", approved ? "APPROVE" : "REJECT");
        if (comment != null && !comment.isBlank()) {
            variables.put("comment", comment);
        }

        taskService.complete(taskId, variables);
    }

    /**
     * Get task details including process variables.
     */
    public Map<String, Object> getTaskDetail(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
            throw new RuntimeException("Task not found: " + taskId);
        return mapTask(task);
    }

    private Map<String, Object> mapTask(Task task) {
        Map<String, Object> vars = runtimeService.getVariables(task.getExecutionId());
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", task.getId());
        result.put("taskName", task.getName());
        result.put("taskCreateTime", task.getCreateTime());
        result.put("processInstanceId", task.getProcessInstanceId());
        result.put("documentType", vars.get("documentType"));
        result.put("documentId", vars.get("documentId"));
        result.put("documentNumber", vars.get("documentNumber"));
        result.put("submittedBy", vars.get("submittedBy"));
        return result;
    }
}
