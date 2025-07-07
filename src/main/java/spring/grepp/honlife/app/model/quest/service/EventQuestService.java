package spring.grepp.honlife.app.model.quest.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.quest.domain.EventQuest;
import spring.grepp.honlife.app.model.quest.dto.EventQuestDTO;
import spring.grepp.honlife.app.model.quest.repos.EventQuestRepository;
import spring.grepp.honlife.infra.util.NotFoundException;


@Service
public class EventQuestService {

    private final EventQuestRepository eventQuestRepository;

    public EventQuestService(final EventQuestRepository eventQuestRepository) {
        this.eventQuestRepository = eventQuestRepository;
    }

    public List<EventQuestDTO> findAll() {
        final List<EventQuest> eventQuests = eventQuestRepository.findAll(Sort.by("id"));
        return eventQuests.stream()
                .map(eventQuest -> mapToDTO(eventQuest, new EventQuestDTO()))
                .toList();
    }

    public EventQuestDTO get(final Long id) {
        return eventQuestRepository.findById(id)
                .map(eventQuest -> mapToDTO(eventQuest, new EventQuestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EventQuestDTO eventQuestDTO) {
        final EventQuest eventQuest = new EventQuest();
        mapToEntity(eventQuestDTO, eventQuest);
        return eventQuestRepository.save(eventQuest).getId();
    }

    public void update(final Long id, final EventQuestDTO eventQuestDTO) {
        final EventQuest eventQuest = eventQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventQuestDTO, eventQuest);
        eventQuestRepository.save(eventQuest);
    }

    public void delete(final Long id) {
        eventQuestRepository.deleteById(id);
    }

    private EventQuestDTO mapToDTO(final EventQuest eventQuest, final EventQuestDTO eventQuestDTO) {
        eventQuestDTO.setCreatedAt(eventQuest.getCreatedAt());
        eventQuestDTO.setUpdatedAt(eventQuest.getUpdatedAt());
        eventQuestDTO.setIsActive(eventQuest.getIsActive());
        eventQuestDTO.setId(eventQuest.getId());
        eventQuestDTO.setKey(eventQuest.getKey());
        eventQuestDTO.setName(eventQuest.getName());
        eventQuestDTO.setInfo(eventQuest.getInfo());
        eventQuestDTO.setStartDate(eventQuest.getStartDate());
        eventQuestDTO.setEndDate(eventQuest.getEndDate());
        return eventQuestDTO;
    }

    private EventQuest mapToEntity(final EventQuestDTO eventQuestDTO, final EventQuest eventQuest) {
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
