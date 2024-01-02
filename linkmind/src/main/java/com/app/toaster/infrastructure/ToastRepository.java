package com.app.toaster.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.Toast;

public interface ToastRepository extends JpaRepository<Toast, Long> {
}
