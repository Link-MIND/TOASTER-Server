package com.app.toaster.popup.entity;

import java.time.LocalDate;
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

	private LocalDate activeStartDate;

	private LocalDate activeEndDate;

	@Column(name = "link_url")
	private String linkUrl;

	@Builder
	private Popup(String image, LocalDate activeStartDate, LocalDate activeEndDate, String linkUrl) {
		this.image = image;
		this.activeStartDate = activeStartDate;
		this.activeEndDate = activeEndDate;
		this.linkUrl = linkUrl;
	}

	public boolean isActivePopup(LocalDate today){
		return !today.isBefore(activeStartDate)&&!today.isAfter(activeEndDate);
	}
}
