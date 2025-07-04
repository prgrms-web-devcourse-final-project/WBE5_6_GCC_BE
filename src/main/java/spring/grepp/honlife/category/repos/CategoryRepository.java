package spring.grepp.honlife.category.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.category.domain.Category;
import spring.grepp.honlife.member.domain.Member;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findFirstByMember(Member member);

}
