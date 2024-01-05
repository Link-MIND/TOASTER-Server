package com.app.toaster.infrastructure;

import com.app.toaster.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.Toast;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ToastRepository extends JpaRepository<Toast, Long> {
    ArrayList<Toast> getAllByCategory(Category category);
    ArrayList<Toast> findByIsReadAndCategory(Boolean isRead, Category category);

    @Modifying
    @Query("UPDATE Toast t SET t.category = null WHERE t.category.categoryId IN :categoryIds")
    void updateCategoryIdsToNull(@Param("categoryIds") List<Long> categoryIds);

}
