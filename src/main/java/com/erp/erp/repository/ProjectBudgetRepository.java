package com.erp.erp.repository;

import com.erp.erp.model.ProjectBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectBudgetRepository extends JpaRepository<ProjectBudget, Long> {
    List<ProjectBudget> findByProjectId(Long projectId);
}
