package spring.grepp.honlife.routine.rest;

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
import spring.grepp.honlife.routine.model.RoutineDTO;
import spring.grepp.honlife.routine.service.RoutineService;


@RestController
@RequestMapping(value = "/api/routines", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoutineResource {

    private final RoutineService routineService;

    public RoutineResource(final RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        return ResponseEntity.ok(routineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(routineService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createRoutine(@RequestBody @Valid final RoutineDTO routineDTO) {
        final Integer createdId = routineService.create(routineDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateRoutine(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final RoutineDTO routineDTO) {
        routineService.update(id, routineDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoutine(@PathVariable(name = "id") final Integer id) {
        routineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
