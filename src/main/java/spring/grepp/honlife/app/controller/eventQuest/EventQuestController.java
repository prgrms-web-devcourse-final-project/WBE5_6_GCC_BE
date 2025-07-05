package spring.grepp.honlife.app.controller.eventQuest;

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
import spring.grepp.honlife.app.model.eventQuest.dto.EventQuestDTO;
import spring.grepp.honlife.app.model.eventQuest.service.EventQuestService;


@RestController
@RequestMapping(value = "/api/eventQuests", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventQuestController {

    private final EventQuestService eventQuestService;

    public EventQuestController(final EventQuestService eventQuestService) {
        this.eventQuestService = eventQuestService;
    }

    @GetMapping
    public ResponseEntity<List<EventQuestDTO>> getAllEventQuests() {
        return ResponseEntity.ok(eventQuestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventQuestDTO> getEventQuest(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(eventQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createEventQuest(
            @RequestBody @Valid final EventQuestDTO eventQuestDTO) {
        final Integer createdId = eventQuestService.create(eventQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateEventQuest(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final EventQuestDTO eventQuestDTO) {
        eventQuestService.update(id, eventQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEventQuest(@PathVariable(name = "id") final Integer id) {
        eventQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
