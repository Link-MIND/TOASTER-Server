package com.app.toaster.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.toaster.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("SELECT c FROM Category c WHERE " +
		"c.user.userId = :userId and " +
		"c.title LIKE CONCAT('%',:query, '%')"
	)
	List<Category> searchCategoriesByQuery(Long userId,String query);
}
