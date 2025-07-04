package spring.grepp.honlife.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.category.domain.Category;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.routine.domain.Routine;


public interface RoutineRepository extends JpaRepository<Routine, Integer> {

    Routine findFirstByMember(Member member);

    Routine findFirstByCategory(Category category);

}
