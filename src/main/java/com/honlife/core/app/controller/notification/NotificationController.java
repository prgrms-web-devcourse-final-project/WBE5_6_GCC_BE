package com.honlife.core.app.controller.notification;

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
import com.honlife.core.app.model.notification.dto.NotificationDTO;
import com.honlife.core.app.model.notification.service.NotificationService;
import com.honlife.core.infra.util.ReferencedException;
import com.honlife.core.infra.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotification(
        @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(notificationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createNotification(
        @RequestBody @Valid final NotificationDTO notificationDTO) {
        final Long createdId = notificationService.create(notificationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateNotification(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final NotificationDTO notificationDTO) {
        notificationService.update(id, notificationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNotification(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = notificationService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
