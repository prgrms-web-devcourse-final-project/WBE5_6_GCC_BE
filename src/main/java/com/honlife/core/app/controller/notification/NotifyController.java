package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotifyResponse;
import com.honlife.core.app.controller.notification.wrapper.NotificationWrapper;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "breathAuth")
@Tag(name = "[회원] 알림 목록",description = "알림 목록 관련 API")
@RestController
@RequestMapping(value = "/api/v1/notify", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotifyController {

    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<NotifyResponse>> readNotification(
            @PathVariable(name = "id") final Long notifyId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return null;
    }

    @PatchMapping("/all/read")
    public ResponseEntity<CommonApiResponse<NotificationWrapper>> readAllNotification(){
        return null;
    }

}
