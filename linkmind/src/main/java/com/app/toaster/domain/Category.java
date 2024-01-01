package com.app.toaster.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	private String title;
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<CategoryManagement> categoryManagements = new ArrayList<>();

	@Builder
	public Category(String title) {
		this.title = title;
	}

	public void updateCategoryIds(List<CategoryManagement> newCategories){
		this.categoryManagements = newCategories; }

}
