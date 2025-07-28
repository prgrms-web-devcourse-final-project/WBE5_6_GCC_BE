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

    /**
     * 해당 멤버와 연관된 첫번째 루틴을 조회
     * @param member
     * @param isActive
     * @return {@link Routine}
     */
    Routine findFirstByMemberAndIsActive(Member member, Boolean isActive);

    Routine findByIdAndMember_email(Long id, String memberEmail);
}
