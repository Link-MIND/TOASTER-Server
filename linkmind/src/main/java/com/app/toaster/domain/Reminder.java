package com.app.toaster.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

	private LocalTime remindTime;

	@Convert(converter = StringListConverter.class)
	private ArrayList<String> remindDates;

	private String comment;

	private Boolean isAlarm;

	@Builder
	public Reminder(User user, Category category, String comment, LocalTime remindTime, ArrayList<String> remindDates, Boolean isAlarm) {
		this.user = user;
		this.category = category;
		this.comment = comment;
		this.remindDates = remindDates;
		this.remindTime = remindTime;
		this.isAlarm = isAlarm;
	}

	public void updateRemindTime(String remindTime){
		this.remindTime = LocalTime.parse(remindTime);
	}

	public void updateRemindDates(ArrayList<String> remindDates){
		this.remindDates = remindDates;
	}

	public void updateComment(String comment){
		this.comment = comment;
	}

}
