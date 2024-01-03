package com.app.toaster.infrastructure;

import com.app.toaster.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.Toast;

import java.util.ArrayList;
import java.util.List;

public interface ToastRepository extends JpaRepository<Toast, Long> {
    ArrayList<Toast> getAllByCategory(Category category);
    ArrayList<Toast> findByIsReadAndCategory(Boolean isRead, Category category);
}
