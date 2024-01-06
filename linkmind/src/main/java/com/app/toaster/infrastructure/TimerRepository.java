package com.app.toaster.infrastructure;

import com.app.toaster.domain.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerRepository extends JpaRepository<Reminder, Long> {
}
