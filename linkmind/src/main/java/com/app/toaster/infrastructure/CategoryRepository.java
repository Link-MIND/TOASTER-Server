package com.app.toaster.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.toaster.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
