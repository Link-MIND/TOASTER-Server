package com.app.toaster.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.Toast;

public interface ToastRepository extends JpaRepository<Toast, Long> {
	void save();
}
