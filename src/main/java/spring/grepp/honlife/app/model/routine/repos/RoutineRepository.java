package spring.grepp.honlife.app.model.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.category.domain.Category;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.routine.domain.Routine;


public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Routine findFirstByMember(Member member);

    Routine findFirstByCategory(Category category);

}
