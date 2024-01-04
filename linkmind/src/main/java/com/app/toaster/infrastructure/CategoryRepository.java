package com.app.toaster.infrastructure;

import java.util.ArrayList;

import com.app.toaster.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.toaster.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    void deleteByCategoryId(Long categoryId);

    ArrayList<Category> findAllByUser(User user);

}
