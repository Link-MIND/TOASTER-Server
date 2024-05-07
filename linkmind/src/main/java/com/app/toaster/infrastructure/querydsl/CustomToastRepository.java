package com.app.toaster.infrastructure.querydsl;

import static com.app.toaster.domain.QToast.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.toaster.domain.Category;
import com.app.toaster.domain.QToast;
import com.app.toaster.domain.Toast;
import com.app.toaster.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomToastRepository {
	private final JPAQueryFactory queryFactory;
	public List<Toast> getAllByCategory(Category category){
		return queryFactory.select(toast)
			.from(toast)
			.where(eqCategoryId(category.getCategoryId()))
			.fetch();
	}

	private BooleanExpression eqCategoryId(Long id){
		if (id == null){
			return null;
		}
		return toast.category.categoryId.eq(id);
	}

	ArrayList<Toast> findByIsReadAndCategory(Boolean isRead, Category category){
		return null;
	};

	ArrayList<Toast> getAllByUser(User user){
		return null;
	}

	List<Toast> getAllByUserOrderByCreatedAtDesc(User user){
		return null;
	}


	ArrayList<Toast> getAllByUserAndIsReadIsTrue(User user){
		return null;
	}


	@Modifying
	@Query("UPDATE Toast t SET t.category = null WHERE t.category.categoryId IN :categoryIds")
	void updateCategoryIdsToNull(@Param("categoryIds") List<Long> categoryIds){
	}


	@Query("SELECT t FROM Toast t WHERE " +
		"t.user.userId = :userId and " +
		"t.title LIKE CONCAT('%',:query, '%')"
	)
	List<Toast> searchToastsByQuery(Long userId, String query){
		return null;
	}


	Long countAllByUser(User user){
		return null;
	}


	Long countALLByUserAndIsReadTrue(User user){
		return null;
	}


	Long countALLByUserAndIsReadFalse(User user){
		return null;
	}


	Long countAllByCategory(Category category){
		return null;
	}


	Long countAllByCategoryAndIsReadTrue(Category category){
		return null;
	}


	Long countAllByCategoryAndIsReadFalse(Category category){
		return null;
	}


	@Query("SELECT COUNT(t) FROM Toast t WHERE t.user.userId = :userId AND t.isRead = false")
	Integer getUnReadToastNumber(Long userId){
		return null;
	}



	@Query("SELECT COUNT(t) FROM Toast t WHERE t.user=:user AND t.createdAt >= :startOfWeek AND t.createdAt <= :endOfWeek")
	Long countAllByCreatedAtThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek,
		@Param("endOfWeek") LocalDateTime endOfWeek, @Param("user") User user){
		return null;
	}


	@Query("SELECT COUNT(t) FROM Toast t WHERE t.user=:user AND t.isRead = true AND t.updateAt >= :startOfWeek AND t.updateAt <= :endOfWeek")
	Long countAllByUpdateAtThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek,
		@Param("endOfWeek") LocalDateTime endOfWeek,
		@Param("user") User user){
		return null;
	}


}
