package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.wrapper.NotificationWrapper;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.notification.service.NotifyListService;
import com.honlife.core.app.model.notification.service.SseService;
import com.honlife.core.infra.response.CommonApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/notify")
public class SseController {

  private final SseService sseService;
  private final MemberService memberService;
  private final NotifyListService notifyListService;


  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(@RequestParam String email, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "https://littlestep-routie.vercel.app");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Content-Type", "text/event-stream");
    response.setHeader("Connection", "keep-alive");


    MemberDTO member = memberService.findMemberByEmail(userDetails.getUsername());
    return sseService.connect(member.getId());
  }

  /** 실제 읽었는지 안읽었는지 확인하는거 */

  @GetMapping("/read")
  public ResponseEntity<CommonApiResponse<Boolean>> markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
    MemberDTO member = memberService.findMemberByEmail(userDetails.getUsername());

    Boolean readtrue = notifyListService.hasUnread(member.getId());

    return ResponseEntity.ok().body(CommonApiResponse.success(readtrue));
  }



}
