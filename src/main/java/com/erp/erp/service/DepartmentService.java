package com.erp.erp.service;

import com.erp.erp.model.Department;
import com.erp.erp.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @SuppressWarnings("null")
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @SuppressWarnings("null")
    public Department updateDepartment(Long id, Department department) {
        return departmentRepository.findById(id)
                .map(existing -> {
                    existing.setName(department.getName());
                    return departmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @SuppressWarnings("null")
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }
}
