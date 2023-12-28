package com.app.toaster.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	private String title;

	private String linkUrl;

	@Builder
	public Toast(User user, String title, String linkUrl){
		this.user= user;
		this.title = title;
		this.linkUrl = linkUrl;
	}

	public void updateTitle(String title){
		this.title = title;
	}
}
