package spring.grepp.honlife.app.model.badge.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.badge.domain.Badge;
import spring.grepp.honlife.app.model.category.domain.Category;


public interface BadgeRepository extends JpaRepository<Badge, Integer> {

    Badge findFirstByCategory(Category category);

}
