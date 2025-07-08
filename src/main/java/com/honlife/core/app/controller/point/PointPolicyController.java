package com.honlife.core.app.controller.point;

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
import com.honlife.core.app.model.point.dto.PointPolicyDTO;
import com.honlife.core.app.model.point.service.PointPolicyService;


@RestController
@RequestMapping(value = "/api/pointPolicies", produces = MediaType.APPLICATION_JSON_VALUE)
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;

    public PointPolicyController(final PointPolicyService pointPolicyService) {
        this.pointPolicyService = pointPolicyService;
    }

    @GetMapping
    public ResponseEntity<List<PointPolicyDTO>> getAllPointPolicies() {
        return ResponseEntity.ok(pointPolicyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointPolicyDTO> getPointPolicy(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(pointPolicyService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPointPolicy(
        @RequestBody @Valid final PointPolicyDTO pointPolicyDTO) {
        final Long createdId = pointPolicyService.create(pointPolicyDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePointPolicy(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final PointPolicyDTO pointPolicyDTO) {
        pointPolicyService.update(id, pointPolicyDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePointPolicy(@PathVariable(name = "id") final Long id) {
        pointPolicyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
