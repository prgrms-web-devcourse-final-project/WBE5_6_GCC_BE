package spring.grepp.honlife.app.controller.quest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.grepp.honlife.app.model.quest.dto.WeeklyQuestDTO;
import spring.grepp.honlife.app.model.quest.service.WeeklyQuestService;


@RestController
@RequestMapping(value = "/api/weeklyQuests", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeeklyQuestController {

    private final WeeklyQuestService weeklyQuestService;

    public WeeklyQuestController(final WeeklyQuestService weeklyQuestService) {
        this.weeklyQuestService = weeklyQuestService;
    }

    @GetMapping
    public ResponseEntity<List<WeeklyQuestDTO>> getAllWeeklyQuests() {
        return ResponseEntity.ok(weeklyQuestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeeklyQuestDTO> getWeeklyQuest(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(weeklyQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createWeeklyQuest(
        @RequestBody @Valid final WeeklyQuestDTO weeklyQuestDTO) {
        final Long createdId = weeklyQuestService.create(weeklyQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateWeeklyQuest(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final WeeklyQuestDTO weeklyQuestDTO) {
        weeklyQuestService.update(id, weeklyQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWeeklyQuest(@PathVariable(name = "id") final Long id) {
        weeklyQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
