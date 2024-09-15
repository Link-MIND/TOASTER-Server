package com.app.toaster.popup.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Popup {

	@Id
	@Column(name = "popup_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT", name = "image")
	private String image;

	@Column(name = "active")
	private boolean active;

	@Column(name = "link_url")
	private String linkUrl;

	@Builder
	private Popup(String image, boolean active, String linkUrl) {
		this.image = image;
		this.active = active;
		this.linkUrl = linkUrl;
	}

	public void updateIsActive(boolean isActive){
		this.active = active;
	}
}
