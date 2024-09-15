package com.app.toaster.popup.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저를 엮을것인가? 그러기에는 userId만 필요함.
 * popup을 엮을 것인가? -> popup이 없어지면 popupmanager도 삭제.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupInvisibleManager {

	@Id
	@Column(name = "popup_manager_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "popup_id")
	private Popup popup;

	private boolean wantToInvisible;

	@Builder
	private PopupInvisibleManager(Long userId, Popup popup, boolean wantToInvisible) {
		this.userId = userId;
		this.popup = popup;
		this.wantToInvisible = wantToInvisible;
	}

	public void updateInvisible(boolean wantToInvisible){
		this.wantToInvisible = wantToInvisible;
	}
}
