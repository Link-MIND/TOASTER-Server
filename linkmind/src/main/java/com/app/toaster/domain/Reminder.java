package com.app.toaster.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reminder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@OneToOne
	@JoinColumn(name = "category")
	private Category category;

	private String title;

	@Builder
	public Reminder(User user, Category category, String title){
		this.user = user;
		this.category = category;
		this.title = title;
	}



}
