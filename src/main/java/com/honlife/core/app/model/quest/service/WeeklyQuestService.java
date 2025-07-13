package com.honlife.core.app.model.quest.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.quest.domain.WeeklyQuest;
import com.honlife.core.app.controller.admin.payload.WeeklyQuestRequest;
import com.honlife.core.app.model.quest.repos.WeeklyQuestRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class WeeklyQuestService {

    private final WeeklyQuestRepository weeklyQuestRepository;

    public WeeklyQuestService(final WeeklyQuestRepository weeklyQuestRepository) {
        this.weeklyQuestRepository = weeklyQuestRepository;
    }

    public List<WeeklyQuestRequest> findAll() {
        final List<WeeklyQuest> weeklyQuests = weeklyQuestRepository.findAll(Sort.by("id"));
        return weeklyQuests.stream()
                .map(weeklyQuest -> mapToDTO(weeklyQuest, new WeeklyQuestRequest()))
                .toList();
    }

    public WeeklyQuestRequest get(final Long id) {
        return weeklyQuestRepository.findById(id)
                .map(weeklyQuest -> mapToDTO(weeklyQuest, new WeeklyQuestRequest()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final WeeklyQuestRequest weeklyQuestRequest) {
        final WeeklyQuest weeklyQuest = new WeeklyQuest();
        mapToEntity(weeklyQuestRequest, weeklyQuest);
        return weeklyQuestRepository.save(weeklyQuest).getId();
    }

    public void update(final Long id, final WeeklyQuestRequest weeklyQuestRequest) {
        final WeeklyQuest weeklyQuest = weeklyQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(weeklyQuestRequest, weeklyQuest);
        weeklyQuestRepository.save(weeklyQuest);
    }

    public void delete(final Long id) {
        weeklyQuestRepository.deleteById(id);
    }

    private WeeklyQuestRequest mapToDTO(final WeeklyQuest weeklyQuest,
            final WeeklyQuestRequest weeklyQuestRequest) {
        weeklyQuestRequest.setCreatedAt(weeklyQuest.getCreatedAt());
        weeklyQuestRequest.setUpdatedAt(weeklyQuest.getUpdatedAt());
        weeklyQuestRequest.setIsActive(weeklyQuest.getIsActive());
        weeklyQuestRequest.setWeeklyQuestId(weeklyQuest.getId());
        weeklyQuestRequest.setKey(weeklyQuest.getKey());
        weeklyQuestRequest.setName(weeklyQuest.getName());
        weeklyQuestRequest.setInfo(weeklyQuest.getInfo());
        return weeklyQuestRequest;
    }

    private WeeklyQuest mapToEntity(final WeeklyQuestRequest weeklyQuestRequest,
            final WeeklyQuest weeklyQuest) {
        weeklyQuest.setCreatedAt(weeklyQuestRequest.getCreatedAt());
        weeklyQuest.setUpdatedAt(weeklyQuestRequest.getUpdatedAt());
        weeklyQuest.setIsActive(weeklyQuestRequest.getIsActive());
        weeklyQuest.setKey(weeklyQuestRequest.getKey());
        weeklyQuest.setName(weeklyQuestRequest.getName());
        weeklyQuest.setInfo(weeklyQuestRequest.getInfo());
        return weeklyQuest;
    }

}
