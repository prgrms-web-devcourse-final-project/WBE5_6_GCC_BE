package com.honlife.core.app.model.quest.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.quest.domain.EventQuest;
import com.honlife.core.app.controller.admin.payload.EventQuestRequest;
import com.honlife.core.app.model.quest.repos.EventQuestRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class EventQuestService {

    private final EventQuestRepository eventQuestRepository;

    public EventQuestService(final EventQuestRepository eventQuestRepository) {
        this.eventQuestRepository = eventQuestRepository;
    }

    public List<EventQuestRequest> findAll() {
        final List<EventQuest> eventQuests = eventQuestRepository.findAll(Sort.by("id"));
        return eventQuests.stream()
                .map(eventQuest -> mapToDTO(eventQuest, new EventQuestRequest()))
                .toList();
    }

    public EventQuestRequest get(final Long id) {
        return eventQuestRepository.findById(id)
                .map(eventQuest -> mapToDTO(eventQuest, new EventQuestRequest()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EventQuestRequest eventQuestDTO) {
        final EventQuest eventQuest = new EventQuest();
        mapToEntity(eventQuestDTO, eventQuest);
        return eventQuestRepository.save(eventQuest).getId();
    }

    public void update(final Long id, final EventQuestRequest eventQuestDTO) {
        final EventQuest eventQuest = eventQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventQuestDTO, eventQuest);
        eventQuestRepository.save(eventQuest);
    }

    public void delete(final Long id) {
        eventQuestRepository.deleteById(id);
    }

    private EventQuestRequest mapToDTO(final EventQuest eventQuest, final EventQuestRequest eventQuestDTO) {
        eventQuestDTO.setCreatedAt(eventQuest.getCreatedAt());
        eventQuestDTO.setUpdatedAt(eventQuest.getUpdatedAt());
        eventQuestDTO.setIsActive(eventQuest.getIsActive());
        eventQuestDTO.setEventQuestId(eventQuest.getId());
        eventQuestDTO.setKey(eventQuest.getKey());
        eventQuestDTO.setName(eventQuest.getName());
        eventQuestDTO.setInfo(eventQuest.getInfo());
        eventQuestDTO.setStartDate(eventQuest.getStartDate());
        eventQuestDTO.setEndDate(eventQuest.getEndDate());
        return eventQuestDTO;
    }

    private EventQuest mapToEntity(final EventQuestRequest eventQuestDTO, final EventQuest eventQuest) {
        eventQuest.setCreatedAt(eventQuestDTO.getCreatedAt());
        eventQuest.setUpdatedAt(eventQuestDTO.getUpdatedAt());
        eventQuest.setIsActive(eventQuestDTO.getIsActive());
        eventQuest.setKey(eventQuestDTO.getKey());
        eventQuest.setName(eventQuestDTO.getName());
        eventQuest.setInfo(eventQuestDTO.getInfo());
        eventQuest.setStartDate(eventQuestDTO.getStartDate());
        eventQuest.setEndDate(eventQuestDTO.getEndDate());
        return eventQuest;
    }

}
