package com.app.toaster.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.app.toaster.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Transactional
    @Modifying
    @Query("delete from Category c where c.categoryId in :ids")
    void deleteALLByCategoryIdInQuery(@Param("ids") List<Long> categoryIds);


    ArrayList<Category> findAllByUser(User user);

}
