package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotifyListResponse;
import com.honlife.core.app.model.notification.dto.NotifyListDTO;
import com.honlife.core.app.model.notification.service.NotifyListService;
import com.honlife.core.infra.response.CommonApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/notify", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotifyController {

  private final NotifyListService notifyListService;



  /**
   * 어떤 알림 읽었는지 보내주는 api
   */
  @PatchMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> readNotification(
      @PathVariable(name ="id")
      final Long notifyId,
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    String userEmail = userDetails.getUsername();
    notifyListService.readNotification(userEmail, notifyId);

    return ResponseEntity.ok(CommonApiResponse.noContent());

  }

  /**
   * 모두 읽음 api
   */
  @PatchMapping("/all")
  public ResponseEntity<CommonApiResponse<Void>> readALlNotification(
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    String userEmail = userDetails.getUsername();
    notifyListService.readAllNotification(userEmail);

    return ResponseEntity.ok(CommonApiResponse.noContent());

  }

  /**
   * 알림 전체 목록 조회
   */
  @GetMapping
  public ResponseEntity<CommonApiResponse<List<NotifyListResponse>>> getALlNotification(
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    String userEmail = userDetails.getUsername();
    List<NotifyListDTO> dto = notifyListService.getAllNotification(userEmail);

    List<NotifyListResponse> responses = dto.stream()
        .map(NotifyListResponse::fromDto)
        .toList();

    return ResponseEntity
        .ok(CommonApiResponse.success(responses));

  }

}
