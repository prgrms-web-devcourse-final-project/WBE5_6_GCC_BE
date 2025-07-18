package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.model.member.model.MemberBadgeDetailDTO;
import com.honlife.core.app.model.member.service.MemberBadgeService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/members/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;

    public MemberBadgeController(final MemberBadgeService memberBadgeService) {
        this.memberBadgeService = memberBadgeService;
    }

    /**
     * 현재 로그인한 회원이 보유하는 모든 업적을 조회하는 API
     * @return List<MemberBadgeResponse> 로그인한 사용자가 보유한 모든 업적에 대한 정보
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<MemberBadgeResponse>>> getAllMemberBadges(
        @AuthenticationPrincipal UserDetails userDetails) {

        try {
            // 1. 현재 로그인한 사용자 이메일 추출
            String email = userDetails.getUsername();

            // 2. Service에서 배지 상세 정보 조회
            List<MemberBadgeDetailDTO> detailDTOs = memberBadgeService.getMemberBadgeDetails(email);

            // 3. DetailDTO → Response 변환
            List<MemberBadgeResponse> responses = detailDTOs.stream()
                .map(MemberBadgeResponse::fromDTO)
                .toList();

            return ResponseEntity.ok(CommonApiResponse.success(responses));

        } catch (Exception e) {
            return ResponseEntity.status(ResponseCode.INTERNAL_SERVER_ERROR.status())
                .body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
        }
    }
}
