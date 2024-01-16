package com.app.toaster.infrastructure;

import com.app.toaster.domain.Reminder;
import com.app.toaster.domain.User;
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



//    @Query("SELECT r FROM Reminder r WHERE :today MEMBER OF r.remindDates")
//    List<Reminder> findAllByRemindDates(@Param("today") Integer today);
}
