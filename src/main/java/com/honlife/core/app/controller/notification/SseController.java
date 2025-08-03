package com.honlife.core.app.controller.notification;

import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.notification.service.NotifyListService;
import com.honlife.core.app.model.notification.service.SseService;
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

  @CrossOrigin(
      origins = "http://localhost:3000",
      allowCredentials = "true"
  )
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(@RequestParam String email, @AuthenticationPrincipal UserDetails userDetails) {

    MemberDTO member = memberService.findMemberByEmail(userDetails.getUsername());
    return sseService.connect(member.getId());
  }

  @PostMapping("/read")
  public ResponseEntity<Void> markAllAsRead(@RequestParam String email) {
    MemberDTO member = memberService.findMemberByEmail(email);

    notifyListService.markAllAsRead(member);
    return ResponseEntity.ok().build();
  }



}
