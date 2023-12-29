package com.app.toaster.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
