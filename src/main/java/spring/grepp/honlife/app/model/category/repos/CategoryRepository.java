package spring.grepp.honlife.app.model.category.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.category.domain.Category;
import spring.grepp.honlife.app.model.member.domain.Member;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByMember(Member member);

}
