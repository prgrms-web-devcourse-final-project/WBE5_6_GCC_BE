package spring.grepp.honlife.app.controller.routine;

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
import spring.grepp.honlife.app.model.routine.dto.RoutineScheduleDTO;
import spring.grepp.honlife.app.model.routine.service.RoutineScheduleService;


@RestController
@RequestMapping(value = "/api/routineSchedules", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoutineScheduleController {

    private final RoutineScheduleService routineScheduleService;

    public RoutineScheduleController(final RoutineScheduleService routineScheduleService) {
        this.routineScheduleService = routineScheduleService;
    }

    @GetMapping
    public ResponseEntity<List<RoutineScheduleDTO>> getAllRoutineSchedules() {
        return ResponseEntity.ok(routineScheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineScheduleDTO> getRoutineSchedule(
        @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(routineScheduleService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoutineSchedule(
        @RequestBody @Valid final RoutineScheduleDTO routineScheduleDTO) {
        final Long createdId = routineScheduleService.create(routineScheduleDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoutineSchedule(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final RoutineScheduleDTO routineScheduleDTO) {
        routineScheduleService.update(id, routineScheduleDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoutineSchedule(@PathVariable(name = "id") final Long id) {
        routineScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
