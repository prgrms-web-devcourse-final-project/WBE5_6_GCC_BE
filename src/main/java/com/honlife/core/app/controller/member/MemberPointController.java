package com.honlife.core.app.controller.member;

import com.honlife.core.infra.response.CommonApiResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.member.service.MemberPointService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members/points", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberPointController {

    private final MemberPointService memberPointService;

    /**
     * 현재 로그인한 멤버의 보유 포인트량을 조회하는 API
     * @param userDetails 사용자 정보 객체
     * @return MemberPointPayload 멤버의 아이디와 현재 보유한 포인트를 담음
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<Map<String, Integer>>> getMemberPoint(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        Integer points = memberPointService.getMemberPoint(userEmail).getPoint();

        return ResponseEntity.ok(CommonApiResponse.success(Map.of("points", points < 0 ? 0 : points)));
    }
}
