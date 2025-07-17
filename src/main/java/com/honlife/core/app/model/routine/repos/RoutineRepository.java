package com.honlife.core.app.model.routine.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.routine.domain.Routine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Routine findFirstByMember(Member member);

    Routine findFirstByCategory(Category category);

    @Query("""
    SELECT r FROM Routine r
    JOIN FETCH r.category c
    WHERE r.member = :member
""")
    List<Routine> findAllByMemberWithCategory(@Param("member") Member member);

    @Query("SELECT r FROM Routine r JOIN FETCH r.category WHERE r.id = :id")
    Optional<Routine> findByIdWithCategory(@Param("id") Long id);

    @Query("SELECT r FROM Routine r WHERE r.id = :id AND r.isActive = true")
    Optional<Routine> findByIdWithMember(@Param("id") Long id);

}
