package spring.grepp.honlife.app.controller.weeklyQeust;

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
import spring.grepp.honlife.app.model.weeklyQuest.dto.WeeklyQuestDTO;
import spring.grepp.honlife.app.model.weeklyQuest.service.WeeklyQuestService;


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
    public ResponseEntity<WeeklyQuestDTO> getWeeklyQuest(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(weeklyQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createWeeklyQuest(
            @RequestBody @Valid final WeeklyQuestDTO weeklyQuestDTO) {
        final Integer createdId = weeklyQuestService.create(weeklyQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateWeeklyQuest(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final WeeklyQuestDTO weeklyQuestDTO) {
        weeklyQuestService.update(id, weeklyQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWeeklyQuest(@PathVariable(name = "id") final Integer id) {
        weeklyQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
