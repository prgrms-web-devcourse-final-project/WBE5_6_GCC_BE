package com.honlife.core.app.model.routine.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.routine.domain.RoutinePreset;


public interface RoutinePresetRepository extends JpaRepository<RoutinePreset, Long> {

    RoutinePreset findFirstByCategory(Category category);

    /**
     * 카테고리를 아이디를 통해 저장된 루틴 프리셋을 조회합니다.
     * @param categoryId 카테고리 아이디
     * @return {@link RoutinePreset} 을 리스트로 반환합니다.
     */
    List<RoutinePreset> getRoutinePresetByCategoryId(Long categoryId);
}
