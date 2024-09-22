package com.app.toaster.popup.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.popup.entity.Popup;

public interface PopupRepository extends JpaRepository<Popup, Long> {
}
