package spring.grepp.honlife.badge.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.badge.domain.Badge;
import spring.grepp.honlife.category.domain.Category;


public interface BadgeRepository extends JpaRepository<Badge, Integer> {

    Badge findFirstByCategory(Category category);

}
