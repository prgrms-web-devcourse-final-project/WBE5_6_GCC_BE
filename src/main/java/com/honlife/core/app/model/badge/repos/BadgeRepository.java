package com.honlife.core.app.model.badge.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.category.domain.Category;


public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Badge findFirstByCategory(Category category);

    /**
     * 배지 키로 배지 조회
     */
    Optional<Badge> findByKey(String key);

    /**
     * 활성화된 모든 배지 조회 (페이지네이션)
     */
    Page<Badge> findPagedByIsActiveTrue(Pageable pageable);

    /**
     * 활성화된 모든 배지 조회
     */
    List<Badge> findAllByIsActiveTrue();

    /**
     * 키로 활성화된 배지 조회
     */
    Optional<Badge> findByKeyAndIsActiveTrue(String key);

    /**
     * 카테고리 id로 활성화된 배지 조회
     */
    List<Badge> findByCategoryIdAndIsActiveTrue(Long categoryId);

    /**
     * 카테고리 id가 null이면 로그인 배지
     */
    List<Badge> findByCategoryIsNullAndIsActiveTrue();
}
