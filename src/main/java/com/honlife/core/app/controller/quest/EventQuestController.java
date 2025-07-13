package com.honlife.core.app.controller.quest;

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
import com.honlife.core.app.controller.admin.payload.EventQuestRequestDTO;
import com.honlife.core.app.model.quest.service.EventQuestService;


@RestController
@RequestMapping(value = "/api/eventQuests", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventQuestController {

    private final EventQuestService eventQuestService;

    public EventQuestController(final EventQuestService eventQuestService) {
        this.eventQuestService = eventQuestService;
    }

    @GetMapping
    public ResponseEntity<List<EventQuestRequestDTO>> getAllEventQuests() {
        return ResponseEntity.ok(eventQuestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventQuestRequestDTO> getEventQuest(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(eventQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEventQuest(
        @RequestBody @Valid final EventQuestRequestDTO eventQuestDTO) {
        final Long createdId = eventQuestService.create(eventQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEventQuest(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final EventQuestRequestDTO eventQuestDTO) {
        eventQuestService.update(id, eventQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEventQuest(@PathVariable(name = "id") final Long id) {
        eventQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
