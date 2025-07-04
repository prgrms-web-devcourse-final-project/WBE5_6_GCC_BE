package spring.grepp.honlife.routine_preset.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.category.domain.Category;
import spring.grepp.honlife.routine_preset.domain.RoutinePreset;


public interface RoutinePresetRepository extends JpaRepository<RoutinePreset, Integer> {

    RoutinePreset findFirstByCategory(Category category);

}
