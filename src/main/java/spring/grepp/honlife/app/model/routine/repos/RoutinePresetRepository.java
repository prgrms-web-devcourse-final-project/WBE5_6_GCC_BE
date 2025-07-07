package spring.grepp.honlife.app.model.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.category.domain.Category;
import spring.grepp.honlife.app.model.routine.domain.RoutinePreset;


public interface RoutinePresetRepository extends JpaRepository<RoutinePreset, Long> {

    RoutinePreset findFirstByCategory(Category category);

}
