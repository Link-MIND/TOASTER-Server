package com.app.toaster.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.toaster.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.toaster.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category deleteByCategoryId(Long categoryId);

    ArrayList<Category> findAllByUser(User user);

}
