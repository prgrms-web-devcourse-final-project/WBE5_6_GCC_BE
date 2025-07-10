package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.ArrayList;
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

@Tag(name = "알림",description = "알림 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    /*
     * 모든 알림 조회 API
     * @return List<NotificationPayload> 모든 알림에 대한 정보
     * */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<NotificationPayload>>> getAllNotifications() {
        List<NotificationPayload> payload = new ArrayList<>();


        return ResponseEntity.ok(CommonApiResponse.success(payload));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotification(
        @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(notificationService.get(id));
    }

    @PostMapping
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
    public ResponseEntity<Void> deleteNotification(@PathVariable(name = "id") final Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
