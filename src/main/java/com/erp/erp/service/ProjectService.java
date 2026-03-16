package com.erp.erp.service;

import com.erp.erp.dto.project.ProjectRequest;
import com.erp.erp.dto.project.ProjectResponse;
import com.erp.erp.model.*;
import com.erp.erp.repository.*;
import com.erp.erp.util.CodeGeneratorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectBudgetRepository projectBudgetRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public ProjectService(ProjectRepository projectRepository, ProjectTaskRepository projectTaskRepository,
            ProjectBudgetRepository projectBudgetRepository, CustomerRepository customerRepository,
            EmployeeRepository employeeRepository, CodeGeneratorUtil codeGeneratorUtil) {
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectBudgetRepository = projectBudgetRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }

        Project project = new Project();
        project.setCode(codeGeneratorUtil.generateCode("PRJ"));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCustomer(customer);
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setBudget(request.getBudget() != null ? request.getBudget() : BigDecimal.ZERO);
        project.setStatus(Project.Status.PLANNED);

        Project savedProject = projectRepository.save(project);

        if (request.getTasks() != null) {
            for (ProjectRequest.ProjectTaskRequest taskReq : request.getTasks()) {
                ProjectTask task = new ProjectTask();
                task.setProject(savedProject);
                task.setName(taskReq.getName());
                task.setDescription(taskReq.getDescription());
                if (taskReq.getAssigneeId() != null) {
                    Employee assignee = employeeRepository.findById(taskReq.getAssigneeId())
                            .orElseThrow(() -> new RuntimeException("Employee not found"));
                    task.setAssignee(assignee);
                }
                task.setStartDate(taskReq.getStartDate());
                task.setEndDate(taskReq.getEndDate());
                task.setStatus(ProjectTask.Status.TODO);
                projectTaskRepository.save(task);
            }
        }

        if (request.getBudgets() != null) {
            for (ProjectRequest.ProjectBudgetRequest budgetReq : request.getBudgets()) {
                ProjectBudget bdg = new ProjectBudget();
                bdg.setProject(savedProject);
                bdg.setCategory(budgetReq.getCategory());
                bdg.setPlannedAmount(
                        budgetReq.getPlannedAmount() != null ? budgetReq.getPlannedAmount() : BigDecimal.ZERO);
                bdg.setActualAmount(BigDecimal.ZERO);
                bdg.setNote(budgetReq.getNote());
                projectBudgetRepository.save(bdg);
            }
        }

        return getProjectById(savedProject.getId()); // Re-fetch for full mappings
    }

    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public ProjectResponse getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public ProjectResponse updateStatus(Long id, Project.Status status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(status);
        return mapToResponse(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectResponse mapToResponse(Project project) {
        List<ProjectTask> tasks = projectTaskRepository.findByProjectId(project.getId());
        List<ProjectBudget> budgets = projectBudgetRepository.findByProjectId(project.getId());

        return ProjectResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .description(project.getDescription())
                .customerId(project.getCustomer() != null ? project.getCustomer().getId() : null)
                .customerName(project.getCustomer() != null ? project.getCustomer().getName() : null)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .budget(project.getBudget())
                .status(project.getStatus().name())
                .createdDate(project.getCreatedDate())
                .tasks(tasks.stream().map(t -> ProjectResponse.ProjectTaskResponse.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .description(t.getDescription())
                        .assigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null)
                        .assigneeName(t.getAssignee() != null ? t.getAssignee().getFullName() : null)
                        .startDate(t.getStartDate())
                        .endDate(t.getEndDate())
                        .status(t.getStatus().name())
                        .build()).collect(Collectors.toList()))
                .budgets(budgets.stream().map(b -> ProjectResponse.ProjectBudgetResponse.builder()
                        .id(b.getId())
                        .category(b.getCategory())
                        .plannedAmount(b.getPlannedAmount())
                        .actualAmount(b.getActualAmount())
                        .note(b.getNote())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
