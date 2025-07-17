package com.honlife.core.app.model.routine.service;

import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.routine.domain.RoutinePreset;
import com.honlife.core.app.model.routine.dto.RoutinePresetDTO;
import com.honlife.core.app.model.routine.repos.RoutinePresetRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@Service
public class RoutinePresetService {

    private final RoutinePresetRepository routinePresetRepository;
    private final CategoryRepository categoryRepository;

    public RoutinePresetService(final RoutinePresetRepository routinePresetRepository,
            final CategoryRepository categoryRepository) {
        this.routinePresetRepository = routinePresetRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RoutinePresetDTO> findAll() {
        final List<RoutinePreset> routinePresets = routinePresetRepository.findAll(Sort.by("id"));
        return routinePresets.stream()
                .map(routinePreset -> mapToDTO(routinePreset, new RoutinePresetDTO()))
                .toList();
    }

    public RoutinePresetDTO get(final Long id) {
        return routinePresetRepository.findById(id)
                .map(routinePreset -> mapToDTO(routinePreset, new RoutinePresetDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
    }

    public Long create(final RoutinePresetDTO routinePresetDTO) {
        final RoutinePreset routinePreset = new RoutinePreset();
        mapToEntity(routinePresetDTO, routinePreset);
        return routinePresetRepository.save(routinePreset).getId();
    }

    public void update(final Long id, final RoutinePresetDTO routinePresetDTO) {
        final RoutinePreset routinePreset = routinePresetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
        mapToEntity(routinePresetDTO, routinePreset);
        routinePresetRepository.save(routinePreset);
    }

    public void delete(final Long id) {
        routinePresetRepository.deleteById(id);
    }

    private RoutinePresetDTO mapToDTO(final RoutinePreset routinePreset,
            final RoutinePresetDTO routinePresetDTO) {
        routinePresetDTO.setCreatedAt(routinePreset.getCreatedAt());
        routinePresetDTO.setUpdatedAt(routinePreset.getUpdatedAt());
        routinePresetDTO.setIsActive(routinePreset.getIsActive());
        routinePresetDTO.setRoutinePresetId(routinePreset.getId());
        routinePresetDTO.setContent(routinePreset.getContent());
        routinePresetDTO.setCategory(routinePreset.getCategory() == null ? null : routinePreset.getCategory().getId());
        return routinePresetDTO;
    }

    private RoutinePreset mapToEntity(final RoutinePresetDTO routinePresetDTO,
            final RoutinePreset routinePreset) {
        routinePreset.setCreatedAt(routinePresetDTO.getCreatedAt());
        routinePreset.setUpdatedAt(routinePresetDTO.getUpdatedAt());
        routinePreset.setIsActive(routinePresetDTO.getIsActive());
        routinePreset.setContent(routinePresetDTO.getContent());
        final Category category = routinePresetDTO.getCategory() == null ? null : categoryRepository.findById(routinePresetDTO.getCategory())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
        routinePreset.setCategory(category);
        return routinePreset;
    }

}
