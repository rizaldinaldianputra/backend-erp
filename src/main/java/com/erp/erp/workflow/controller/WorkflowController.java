package com.erp.erp.workflow.controller;

import com.erp.erp.security.SecurityUtil;
import com.erp.erp.workflow.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submit(@RequestBody Map<String, Object> body) {
        String documentType = (String) body.get("documentType");
        Long documentId = Long.valueOf(body.get("documentId").toString());
        String documentNumber = (String) body.get("documentNumber");
        String submittedBy = SecurityUtil.getCurrentUsername();

        String processInstanceId = workflowService.startApproval(documentType, documentId, documentNumber, submittedBy);
        return ResponseEntity.ok(Map.of(
                "message", "Document submitted for approval",
                "processInstanceId", processInstanceId,
                "status", "PENDING_APPROVAL"));
    }

    /**
     * Get all tasks pending for APPROVER group (used by Task inbox page).
     * GET /api/workflow/tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Map<String, Object>>> getAllTasks() {
        String username = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(workflowService.getAllPendingTasks(username));
    }

    /**
     * Get all documents submitted by the current logged-in user.
     * GET /api/workflow/my-submissions
     */
    @GetMapping("/my-submissions")
    public ResponseEntity<List<Map<String, Object>>> getMySubmissions() {
        String userId = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(workflowService.getMySubmittedTasks(userId));
    }

    /**
     * Get detail of a specific task.
     * GET /api/workflow/tasks/{taskId}
     */
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Map<String, Object>> getTaskDetail(@PathVariable String taskId) {
        return ResponseEntity.ok(workflowService.getTaskDetail(taskId));
    }

    /**
     * Complete a task — approve or reject.
     * POST /api/workflow/tasks/{taskId}/complete
     * body: { approved: true/false, comment: "..." }
     */
    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable String taskId,
            @RequestBody Map<String, Object> body) {

        boolean approved = Boolean.parseBoolean(body.get("approved").toString());
        String comment = body.containsKey("comment") ? (String) body.get("comment") : null;

        workflowService.completeTask(taskId, approved, comment);

        return ResponseEntity.ok(Map.of(
                "message", approved ? "Document approved successfully" : "Document rejected",
                "taskId", taskId));
    }
}
