package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.project.ProjectRequest;
import com.erp.erp.dto.project.ProjectResponse;
import com.erp.erp.model.Project;
import com.erp.erp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "Manage Projects, Tasks, and Budgets")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new Project")
    public ResponseEntity<ApiResponseDto<ProjectResponse>> createProject(@RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.ok(ApiResponseDto.<ProjectResponse>builder()
                .status("success")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all Projects")
    public ResponseEntity<ApiResponseDto<Page<ProjectResponse>>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<ProjectResponse>>builder()
                .status("success")
                .data(projectService.getAllProjects(pageable))
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Project by ID")
    public ResponseEntity<ApiResponseDto<ProjectResponse>> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<ProjectResponse>builder()
                .status("success")
                .data(projectService.getProjectById(id))
                .build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update Project Status")
    public ResponseEntity<ApiResponseDto<ProjectResponse>> updateStatus(@PathVariable Long id,
            @RequestParam Project.Status status) {
        return ResponseEntity.ok(ApiResponseDto.<ProjectResponse>builder()
                .status("success")
                .data(projectService.updateStatus(id, status))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Project")
    public ResponseEntity<ApiResponseDto<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .status("success")
                .message("Project deleted successfully")
                .build());
    }
}
