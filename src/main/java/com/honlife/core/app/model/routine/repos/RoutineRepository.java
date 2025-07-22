package com.honlife.core.app.model.routine.repos;

import java.util.List;
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

    /**
     * category 아이디를 통해 해당 카테고리를 참조하는 루틴을 조회합니다.
     * @param categoryId 카테고리 아이디
     * @return {@link Routine} 를 리스트로 반환
     */
    List<Routine> findAllByCategory_Id(Long categoryId);
}
