package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberPayload;
import com.honlife.core.app.controller.member.payload.MemberPointPayload;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import com.honlife.core.app.model.member.model.MemberPointDTO;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.infra.util.ReferencedException;
import com.honlife.core.infra.util.ReferencedWarning;

@Tag(name="회원 보유 포인트", description = "현재 로그인한 회원이 보유하고 있는 포인트 관련 API 입니다.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/members/point", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberPointController {

    private final MemberPointService memberPointService;

    public MemberPointController(final MemberPointService memberPointService) {
        this.memberPointService = memberPointService;
    }

    /**
     * 현재 로그인한 멤버의 보유 포인트량을 조회하는 API
     * @return MemberPointPayload 멤버의 아이디와 현재 보유한 포인트를 담음
     */
    @GetMapping
    @Operation(summary = "로그인된 회원의 보유 포인트 조회", description = "로그인된 사용자의 보유 포인트를 조회합니다.")
    public ResponseEntity<CommonApiResponse<MemberPointPayload>> getMemberPoint() {
        MemberPointPayload memberPointPayload
            = MemberPointPayload.builder()
            .email("user01@test.com")
            .currentPoint(10000L)
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(memberPointPayload));
    }
}
