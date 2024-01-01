package com.app.toaster.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.CategoryManagement;
import com.app.toaster.domain.Toast;

public interface CategoryManagementRepository extends JpaRepository<CategoryManagement, Long> {

	Long deleteAllByToast(Toast toast);
}
