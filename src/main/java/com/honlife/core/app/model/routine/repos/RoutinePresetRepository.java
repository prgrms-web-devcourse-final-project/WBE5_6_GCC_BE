package com.honlife.core.app.model.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.routine.domain.RoutinePreset;


public interface RoutinePresetRepository extends JpaRepository<RoutinePreset, Long> {

    RoutinePreset findFirstByCategory(Category category);

}
