package com.honlife.core.app.model.quest.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.quest.domain.WeeklyQuest;
import com.honlife.core.app.model.quest.dto.WeeklyQuestRequestDTO;
import com.honlife.core.app.model.quest.repos.WeeklyQuestRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class WeeklyQuestService {

    private final WeeklyQuestRepository weeklyQuestRepository;

    public WeeklyQuestService(final WeeklyQuestRepository weeklyQuestRepository) {
        this.weeklyQuestRepository = weeklyQuestRepository;
    }

    public List<WeeklyQuestRequestDTO> findAll() {
        final List<WeeklyQuest> weeklyQuests = weeklyQuestRepository.findAll(Sort.by("id"));
        return weeklyQuests.stream()
                .map(weeklyQuest -> mapToDTO(weeklyQuest, new WeeklyQuestRequestDTO()))
                .toList();
    }

    public WeeklyQuestRequestDTO get(final Long id) {
        return weeklyQuestRepository.findById(id)
                .map(weeklyQuest -> mapToDTO(weeklyQuest, new WeeklyQuestRequestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final WeeklyQuestRequestDTO weeklyQuestRequestDTO) {
        final WeeklyQuest weeklyQuest = new WeeklyQuest();
        mapToEntity(weeklyQuestRequestDTO, weeklyQuest);
        return weeklyQuestRepository.save(weeklyQuest).getId();
    }

    public void update(final Long id, final WeeklyQuestRequestDTO weeklyQuestRequestDTO) {
        final WeeklyQuest weeklyQuest = weeklyQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(weeklyQuestRequestDTO, weeklyQuest);
        weeklyQuestRepository.save(weeklyQuest);
    }

    public void delete(final Long id) {
        weeklyQuestRepository.deleteById(id);
    }

    private WeeklyQuestRequestDTO mapToDTO(final WeeklyQuest weeklyQuest,
            final WeeklyQuestRequestDTO weeklyQuestRequestDTO) {
        weeklyQuestRequestDTO.setCreatedAt(weeklyQuest.getCreatedAt());
        weeklyQuestRequestDTO.setUpdatedAt(weeklyQuest.getUpdatedAt());
        weeklyQuestRequestDTO.setIsActive(weeklyQuest.getIsActive());
        weeklyQuestRequestDTO.setWeeklyQuestId(weeklyQuest.getId());
        weeklyQuestRequestDTO.setKey(weeklyQuest.getKey());
        weeklyQuestRequestDTO.setName(weeklyQuest.getName());
        weeklyQuestRequestDTO.setInfo(weeklyQuest.getInfo());
        return weeklyQuestRequestDTO;
    }

    private WeeklyQuest mapToEntity(final WeeklyQuestRequestDTO weeklyQuestRequestDTO,
            final WeeklyQuest weeklyQuest) {
        weeklyQuest.setCreatedAt(weeklyQuestRequestDTO.getCreatedAt());
        weeklyQuest.setUpdatedAt(weeklyQuestRequestDTO.getUpdatedAt());
        weeklyQuest.setIsActive(weeklyQuestRequestDTO.getIsActive());
        weeklyQuest.setKey(weeklyQuestRequestDTO.getKey());
        weeklyQuest.setName(weeklyQuestRequestDTO.getName());
        weeklyQuest.setInfo(weeklyQuestRequestDTO.getInfo());
        return weeklyQuest;
    }

}
