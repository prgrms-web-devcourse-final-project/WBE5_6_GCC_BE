package com.honlife.core.app.model.badge.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.category.domain.Category;


public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Badge findFirstByCategory(Category category);

}
