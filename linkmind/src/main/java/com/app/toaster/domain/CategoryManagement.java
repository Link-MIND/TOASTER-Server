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
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CategoryManagement {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long managedId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managed_category")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managed_toast")
	private Toast toast;

	@Builder
	public CategoryManagement(Category category, Toast toast){
		this.category = category;
		this.toast = toast;
	}
}
