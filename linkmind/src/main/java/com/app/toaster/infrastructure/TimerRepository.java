package com.app.toaster.infrastructure;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.Reminder;
import com.app.toaster.domain.User;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TimerRepository extends JpaRepository<Reminder, Long> {

    ArrayList<Reminder> findAllByUser(User user);
    void deleteAllByUser(User user);

    ArrayList<Reminder> findAllByCategory(Category category);

    Reminder findByCategory_CategoryId(Long categoryId);
}
