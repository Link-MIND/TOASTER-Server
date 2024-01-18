package com.app.toaster.infrastructure;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.Link;
import com.app.toaster.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("SELECT e FROM Link e ORDER BY FUNCTION('RAND') FETCH FIRST 3 ROWS ONLY")
    List<Link> findRandom3Links();

    List<Link> findAllByThisWeekLinkIsTrue();


}
