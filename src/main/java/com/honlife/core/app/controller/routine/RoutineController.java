package com.honlife.core.app.controller.routine;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import com.honlife.core.app.model.routine.dto.RoutineDTO;
import com.honlife.core.app.model.routine.service.RoutineService;
import com.honlife.core.infra.util.ReferencedException;
import com.honlife.core.infra.util.ReferencedWarning;

@Tag(name = "루틴", description = "루틴 관련 api 입니다.")
@RestController
@RequestMapping(value = "/api/v1/routines", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(final RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        return ResponseEntity.ok(routineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(routineService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoutine(@RequestBody @Valid final RoutineDTO routineDTO) {
        final Long createdId = routineService.create(routineDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoutine(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final RoutineDTO routineDTO) {
        routineService.update(id, routineDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoutine(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = routineService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        routineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
