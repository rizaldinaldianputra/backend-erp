package com.erp.erp.service;

import com.erp.erp.dto.project.TimesheetRequest;
import com.erp.erp.dto.project.TimesheetResponse;
import com.erp.erp.model.Employee;
import com.erp.erp.model.Project;
import com.erp.erp.model.ProjectTask;
import com.erp.erp.model.Timesheet;
import com.erp.erp.repository.EmployeeRepository;
import com.erp.erp.repository.ProjectRepository;
import com.erp.erp.repository.ProjectTaskRepository;
import com.erp.erp.repository.TimesheetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public TimesheetService(TimesheetRepository timesheetRepository, EmployeeRepository employeeRepository,
            ProjectRepository projectRepository, ProjectTaskRepository projectTaskRepository) {
        this.timesheetRepository = timesheetRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
    }

    public TimesheetResponse createTimesheet(TimesheetRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
        }

        ProjectTask task = null;
        if (request.getProjectTaskId() != null) {
            task = projectTaskRepository.findById(request.getProjectTaskId())
                    .orElseThrow(() -> new RuntimeException("Project Task not found"));
        }

        Timesheet timesheet = new Timesheet();
        timesheet.setEmployee(employee);
        timesheet.setProject(project);
        timesheet.setTask(task);
        timesheet.setDate(request.getDate());
        timesheet.setHoursWorked(request.getHoursWorked());
        timesheet.setDescription(request.getDescription());
        timesheet.setStatus(Timesheet.Status.SUBMITTED);

        return mapToResponse(timesheetRepository.save(timesheet));
    }

    public Page<TimesheetResponse> getAllTimesheets(Pageable pageable) {
        return timesheetRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public TimesheetResponse getTimesheetById(Long id) {
        return timesheetRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
    }

    public TimesheetResponse updateStatus(Long id, Timesheet.Status status) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
        timesheet.setStatus(status);
        return mapToResponse(timesheetRepository.save(timesheet));
    }

    public void deleteTimesheet(Long id) {
        timesheetRepository.deleteById(id);
    }

    private TimesheetResponse mapToResponse(Timesheet t) {
        return TimesheetResponse.builder()
                .id(t.getId())
                .employeeId(t.getEmployee().getId())
                .employeeName(t.getEmployee().getFullName())
                .projectId(t.getProject() != null ? t.getProject().getId() : null)
                .projectName(t.getProject() != null ? t.getProject().getName() : null)
                .projectTaskId(t.getTask() != null ? t.getTask().getId() : null)
                .taskName(t.getTask() != null ? t.getTask().getName() : null)
                .date(t.getDate())
                .hoursWorked(t.getHoursWorked())
                .description(t.getDescription())
                .status(t.getStatus().name())
                .submittedAt(t.getSubmittedAt())
                .build();
    }
}
