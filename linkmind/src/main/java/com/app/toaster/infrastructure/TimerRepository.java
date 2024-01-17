package com.app.toaster.infrastructure;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.Reminder;
import com.app.toaster.domain.User;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public interface TimerRepository extends JpaRepository<Reminder, Long> {

    ArrayList<Reminder> findAllByUser(User user);

    void deleteAllByUser(User user);

    ArrayList<Reminder> findAllByCategory(Category category);

    Reminder findByCategory_CategoryId(Long categoryId);

    @Query("select r.category from Reminder r where r.id = :id")
    Category findCategoryByReminderId(@Param("id") Long reminderId);


}