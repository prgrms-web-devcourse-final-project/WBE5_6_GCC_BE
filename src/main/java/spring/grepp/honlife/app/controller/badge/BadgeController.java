package spring.grepp.honlife.app.controller.badge;

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
import spring.grepp.honlife.app.model.badge.dto.BadgeDTO;
import spring.grepp.honlife.app.model.badge.service.BadgeService;
import spring.grepp.honlife.infra.util.ReferencedException;
import spring.grepp.honlife.infra.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(final BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        return ResponseEntity.ok(badgeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeDTO> getBadge(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(badgeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBadge(@RequestBody @Valid final BadgeDTO badgeDTO) {
        final Long createdId = badgeService.create(badgeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBadge(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final BadgeDTO badgeDTO) {
        badgeService.update(id, badgeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBadge(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = badgeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        badgeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
