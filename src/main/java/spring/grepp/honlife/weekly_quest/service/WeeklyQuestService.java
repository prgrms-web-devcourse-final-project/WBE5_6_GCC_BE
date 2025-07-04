package spring.grepp.honlife.weekly_quest.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.util.NotFoundException;
import spring.grepp.honlife.weekly_quest.domain.WeeklyQuest;
import spring.grepp.honlife.weekly_quest.model.WeeklyQuestDTO;
import spring.grepp.honlife.weekly_quest.repos.WeeklyQuestRepository;


@Service
public class WeeklyQuestService {

    private final WeeklyQuestRepository weeklyQuestRepository;

    public WeeklyQuestService(final WeeklyQuestRepository weeklyQuestRepository) {
        this.weeklyQuestRepository = weeklyQuestRepository;
    }

    public List<WeeklyQuestDTO> findAll() {
        final List<WeeklyQuest> weeklyQuests = weeklyQuestRepository.findAll(Sort.by("id"));
        return weeklyQuests.stream()
                .map(weeklyQuest -> mapToDTO(weeklyQuest, new WeeklyQuestDTO()))
                .toList();
    }

    public WeeklyQuestDTO get(final Integer id) {
        return weeklyQuestRepository.findById(id)
                .map(weeklyQuest -> mapToDTO(weeklyQuest, new WeeklyQuestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final WeeklyQuestDTO weeklyQuestDTO) {
        final WeeklyQuest weeklyQuest = new WeeklyQuest();
        mapToEntity(weeklyQuestDTO, weeklyQuest);
        return weeklyQuestRepository.save(weeklyQuest).getId();
    }

    public void update(final Integer id, final WeeklyQuestDTO weeklyQuestDTO) {
        final WeeklyQuest weeklyQuest = weeklyQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(weeklyQuestDTO, weeklyQuest);
        weeklyQuestRepository.save(weeklyQuest);
    }

    public void delete(final Integer id) {
        weeklyQuestRepository.deleteById(id);
    }

    private WeeklyQuestDTO mapToDTO(final WeeklyQuest weeklyQuest,
            final WeeklyQuestDTO weeklyQuestDTO) {
        weeklyQuestDTO.setCreatedAt(weeklyQuest.getCreatedAt());
        weeklyQuestDTO.setUpdatedAt(weeklyQuest.getUpdatedAt());
        weeklyQuestDTO.setIsActive(weeklyQuest.getIsActive());
        weeklyQuestDTO.setId(weeklyQuest.getId());
        weeklyQuestDTO.setKey(weeklyQuest.getKey());
        weeklyQuestDTO.setName(weeklyQuest.getName());
        weeklyQuestDTO.setInfo(weeklyQuest.getInfo());
        return weeklyQuestDTO;
    }

    private WeeklyQuest mapToEntity(final WeeklyQuestDTO weeklyQuestDTO,
            final WeeklyQuest weeklyQuest) {
        weeklyQuest.setCreatedAt(weeklyQuestDTO.getCreatedAt());
        weeklyQuest.setUpdatedAt(weeklyQuestDTO.getUpdatedAt());
        weeklyQuest.setIsActive(weeklyQuestDTO.getIsActive());
        weeklyQuest.setKey(weeklyQuestDTO.getKey());
        weeklyQuest.setName(weeklyQuestDTO.getName());
        weeklyQuest.setInfo(weeklyQuestDTO.getInfo());
        return weeklyQuest;
    }

}
