package com.erp.erp.dto;

import com.erp.erp.model.Department;
import com.erp.erp.model.Position;
import com.erp.erp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String employeeCode;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String birthPlace;
    private String gender;

    private String nik;
    private String kkNumber;

    private LocalDate joinDate;

    private Department department;
    private Position position;

    private String phone;
    private String email;
    private String address;

    private String lastEducation;

    private User user; // include full login user object
}
