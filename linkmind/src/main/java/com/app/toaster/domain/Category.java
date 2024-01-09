package com.app.toaster.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Size(max = 10, message = "클립의 이름은 최대 10자까지 입력 가능해요!")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private int priority;

	private LocalDateTime latestReadTime;

	@Builder
	public Category(String title, User user, int priority) {
		this.title = title;
		this.user = user;
		this.priority = priority;
	}

	public int getPriority(){
		return priority;
	}

	public void updateCategoryName(String newTitle){ this.title = newTitle;}

	public void updateCategoryPriority(int newPriority){
		this.priority = newPriority;}

	public void updateLatestReadTime(LocalDateTime now){
		this.latestReadTime = now;
	}
}

