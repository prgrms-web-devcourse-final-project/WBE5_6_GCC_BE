package com.honlife.core.app.model.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.routine.domain.Routine;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface RoutineRepository extends JpaRepository<Routine, Long>, RoutineRepositoryCustom {

    Routine findFirstByMember(Member member);

    Routine findFirstByCategory(Category category);
}
