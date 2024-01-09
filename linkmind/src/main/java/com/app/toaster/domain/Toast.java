package com.app.toaster.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Toast {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	private String title;

	private String linkUrl;

	private Boolean isRead;

	@Builder
	public Toast(User user, Category category, String title, String linkUrl) {
		this.category = category;
		this.user = user;
		this.title = title;
		this.linkUrl = linkUrl;
		this.isRead = false;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public void updateCategory(Category category){ this.category = category;}



}
