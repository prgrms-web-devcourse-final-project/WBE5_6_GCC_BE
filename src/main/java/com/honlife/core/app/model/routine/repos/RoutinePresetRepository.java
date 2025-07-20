package com.honlife.core.app.model.routine.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.routine.domain.RoutinePreset;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoutinePresetRepository extends JpaRepository<RoutinePreset, Long> {

    RoutinePreset findFirstByCategory(Category category);

    /**
     * 카테고리를 아이디를 통해 저장된 루틴 프리셋을 조회합니다.
     * @param categoryId 카테고리 아이디
     * @return {@link RoutinePreset} 을 리스트로 반환합니다.
     */
    List<RoutinePreset> getRoutinePresetByCategoryId(Long categoryId);

    /** 지연 로딩으로 인해 fetchjoin을 사용하였습니다 **/
    @Query("SELECT rp FROM RoutinePreset rp JOIN FETCH rp.category WHERE (:categoryId IS NULL OR rp.category.id = :categoryId)")
    List<RoutinePreset> getRoutinePresetWithCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT rp FROM RoutinePreset rp JOIN FETCH rp.category")
    List<RoutinePreset> findAllWithCategory();

    @Query("SELECT rp FROM RoutinePreset rp JOIN FETCH rp.category WHERE rp.id = :id")
    Optional<RoutinePreset> findWithCategoryById(@Param("id") Long id);

}
