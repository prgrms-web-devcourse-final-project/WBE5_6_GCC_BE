package spring.grepp.honlife.point_log.rest;

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
import spring.grepp.honlife.point_log.model.PointLogDTO;
import spring.grepp.honlife.point_log.service.PointLogService;


@RestController
@RequestMapping(value = "/api/pointLogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class PointLogResource {

    private final PointLogService pointLogService;

    public PointLogResource(final PointLogService pointLogService) {
        this.pointLogService = pointLogService;
    }

    @GetMapping
    public ResponseEntity<List<PointLogDTO>> getAllPointLogs() {
        return ResponseEntity.ok(pointLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointLogDTO> getPointLog(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(pointLogService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createPointLog(
            @RequestBody @Valid final PointLogDTO pointLogDTO) {
        final Integer createdId = pointLogService.create(pointLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePointLog(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final PointLogDTO pointLogDTO) {
        pointLogService.update(id, pointLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePointLog(@PathVariable(name = "id") final Integer id) {
        pointLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
