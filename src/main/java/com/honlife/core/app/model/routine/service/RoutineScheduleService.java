package com.honlife.core.app.model.routine.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.withdraw.dto.RoutineScheduleDTO;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class RoutineScheduleService {

    private final RoutineScheduleRepository routineScheduleRepository;
    private final RoutineRepository routineRepository;

    public RoutineScheduleService(final RoutineScheduleRepository routineScheduleRepository,
            final RoutineRepository routineRepository) {
        this.routineScheduleRepository = routineScheduleRepository;
        this.routineRepository = routineRepository;
    }

    public List<RoutineScheduleDTO> findAll() {
        final List<RoutineSchedule> routineSchedules = routineScheduleRepository.findAll(Sort.by("id"));
        return routineSchedules.stream()
                .map(routineSchedule -> mapToDTO(routineSchedule, new RoutineScheduleDTO()))
                .toList();
    }

    public RoutineScheduleDTO get(final Long id) {
        return routineScheduleRepository.findById(id)
                .map(routineSchedule -> mapToDTO(routineSchedule, new RoutineScheduleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoutineScheduleDTO routineScheduleDTO) {
        final RoutineSchedule routineSchedule = new RoutineSchedule();
        mapToEntity(routineScheduleDTO, routineSchedule);
        return routineScheduleRepository.save(routineSchedule).getId();
    }

    public void update(final Long id, final RoutineScheduleDTO routineScheduleDTO) {
        final RoutineSchedule routineSchedule = routineScheduleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(routineScheduleDTO, routineSchedule);
        routineScheduleRepository.save(routineSchedule);
    }

    public void delete(final Long id) {
        routineScheduleRepository.deleteById(id);
    }

    private RoutineScheduleDTO mapToDTO(final RoutineSchedule routineSchedule,
            final RoutineScheduleDTO routineScheduleDTO) {
        routineScheduleDTO.setId(routineSchedule.getId());
        routineScheduleDTO.setDate(routineSchedule.getDate());
        routineScheduleDTO.setIsDone(routineSchedule.getIsDone());
        routineScheduleDTO.setCreatedAt(routineSchedule.getCreatedAt());
        routineScheduleDTO.setRoutine(routineSchedule.getRoutine() == null ? null : routineSchedule.getRoutine().getId());
        return routineScheduleDTO;
    }

    private RoutineSchedule mapToEntity(final RoutineScheduleDTO routineScheduleDTO,
            final RoutineSchedule routineSchedule) {
        routineSchedule.setDate(routineScheduleDTO.getDate());
        routineSchedule.setIsDone(routineScheduleDTO.getIsDone());
        routineSchedule.setCreatedAt(routineScheduleDTO.getCreatedAt());
        final Routine routine = routineScheduleDTO.getRoutine() == null ? null : routineRepository.findById(routineScheduleDTO.getRoutine())
                .orElseThrow(() -> new NotFoundException("routine not found"));
        routineSchedule.setRoutine(routine);
        return routineSchedule;
    }

}
