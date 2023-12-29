package com.app.toaster.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.CategoryManagement;

public interface CategoryManagementRepository extends JpaRepository<CategoryManagement, Long> {
}
