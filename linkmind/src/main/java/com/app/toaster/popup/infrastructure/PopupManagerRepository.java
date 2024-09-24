package com.app.toaster.popup.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.popup.entity.Popup;
import com.app.toaster.popup.entity.PopupInvisibleManager;

public interface PopupManagerRepository extends JpaRepository<PopupInvisibleManager, Long> {
	List<PopupInvisibleManager> findByUserId(Long userId);
	Optional<PopupInvisibleManager> findByUserIdAndPopup(Long userId, Popup popup);

	void deleteAllByUserId(Long userId);
}
