package com.honlife.core.app.model.routine.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.routine.domain.Routine;


public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Routine findFirstByMember(Member member);

    Routine findFirstByCategory(Category category);

    List<Routine> findAllByMember(Member member);
}
