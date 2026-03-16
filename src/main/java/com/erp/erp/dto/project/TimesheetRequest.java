package com.erp.erp.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetRequest {

    private Long employeeId;
    private Long projectId;
    private Long projectTaskId;
    private LocalDate date;
    private BigDecimal hoursWorked;
    private String description;

}
