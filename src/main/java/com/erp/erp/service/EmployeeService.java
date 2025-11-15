package com.erp.erp.service;

import com.erp.erp.model.Employee;
import com.erp.erp.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @SuppressWarnings("null")
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @SuppressWarnings("null")
    public Employee updateEmployee(Long id, Employee employee) {
        return employeeRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(employee.getFirstName());
                    existing.setLastName(employee.getLastName());

                    existing.setEmail(employee.getEmail());
                    existing.setDepartment(employee.getDepartment());
                    existing.setPosition(employee.getPosition());
                    existing.setPhone(employee.getPhone());
                    existing.setJoinDate(employee.getJoinDate());
                    return employeeRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @SuppressWarnings("null")
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public List<Employee> getEmployeesByPosition(Long positionId) {
        return employeeRepository.findByPositionId(positionId);
    }
}
