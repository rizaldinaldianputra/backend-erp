package com.erp.erp.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long projectId;
    private String projectName;
    private Long projectTaskId;
    private String taskName;
    private LocalDate date;
    private BigDecimal hoursWorked;
    private String description;
    private String status;
    private LocalDateTime submittedAt;

}
