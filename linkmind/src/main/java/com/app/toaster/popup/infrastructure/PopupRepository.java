package com.app.toaster.popup.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.popup.entity.Popup;

public interface PopupRepository extends JpaRepository<Popup, Long> {
	Optional<Popup> findByIdAndActive(Long id, boolean active);
}
