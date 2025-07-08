package com.honlife.core.app.controller.routine;

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
import com.honlife.core.app.model.routine.dto.RoutinePresetDTO;
import com.honlife.core.app.model.routine.service.RoutinePresetService;


@RestController
@RequestMapping(value = "/api/v1/routinePresets", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoutinePresetResource {

    private final RoutinePresetService routinePresetService;

    public RoutinePresetResource(final RoutinePresetService routinePresetService) {
        this.routinePresetService = routinePresetService;
    }

    @GetMapping
    public ResponseEntity<List<RoutinePresetDTO>> getAllRoutinePresets() {
        return ResponseEntity.ok(routinePresetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutinePresetDTO> getRoutinePreset(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(routinePresetService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoutinePreset(
            @RequestBody @Valid final RoutinePresetDTO routinePresetDTO) {
        final Long createdId = routinePresetService.create(routinePresetDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoutinePreset(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoutinePresetDTO routinePresetDTO) {
        routinePresetService.update(id, routinePresetDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoutinePreset(@PathVariable(name = "id") final Long id) {
        routinePresetService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
