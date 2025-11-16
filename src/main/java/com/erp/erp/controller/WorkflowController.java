package com.erp.erp.controller;

import com.erp.erp.dto.TaskResponse;
import com.erp.erp.model.User;
import com.erp.erp.security.SecurityUtil;
import com.erp.erp.workflow.engine.WorkflowTaskService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@Tag(name = "Workflow Camunda", description = "Manage company Product data")

public class WorkflowController {

    private final WorkflowTaskService workflowTaskService;

    public WorkflowController(WorkflowTaskService workflowTaskService) {
        this.workflowTaskService = workflowTaskService;
    }

    // 🔹 List semua task untuk user login
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks() {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<TaskResponse> tasks = workflowTaskService.listTasksForUser(currentUser.getUsername());
        return ResponseEntity.ok(tasks);
    }

    // 🔹 Assign task ke user lain (optional)
    @PostMapping("/tasks/{taskId}/assign")
    public ResponseEntity<String> assignTask(@PathVariable String taskId,
            @RequestParam String username) {
        workflowTaskService.assignTask(taskId, username);
        return ResponseEntity.ok("Task " + taskId + " assigned to " + username);
    }

    // 🔹 Approve task
    @PostMapping("/tasks/{taskId}/approve")
    public ResponseEntity<String> approveTask(@PathVariable String taskId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Map<String, Object> vars = Map.of(
                "approved", true,
                "username", currentUser.getUsername());

        workflowTaskService.completeTask(taskId, vars);

        return ResponseEntity.ok("Task " + taskId + " approved");
    }

    // 🔹 Reject task
    @PostMapping("/tasks/{taskId}/reject")
    public ResponseEntity<String> rejectTask(@PathVariable String taskId) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Map<String, Object> vars = Map.of(
                "approved", false,
                "username", currentUser.getUsername());

        workflowTaskService.completeTask(taskId, vars);

        return ResponseEntity.ok("Task " + taskId + " rejected");
    }

    // 🔹 Complete task dengan variables tambahan (opsional)
    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<String> completeTask(@PathVariable String taskId,
            @RequestBody Map<String, Object> vars) {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Tambahkan username ke map vars
        vars.put("username", currentUser.getUsername());

        workflowTaskService.completeTask(taskId, vars); // ✅ sesuai signature

        return ResponseEntity.ok("Task " + taskId + " completed with vars: " + vars);
    }

}
