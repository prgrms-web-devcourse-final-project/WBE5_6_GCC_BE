package com.honlife.core.app.controller.member;

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
import com.honlife.core.app.model.member.model.MemberBadgeDTO;
import com.honlife.core.app.model.member.service.MemberBadgeService;

@Tag(name="회원 보유 업적", description = "현재 로그인한 회원이 보유하고 있는 업적 관련 API 입니다.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/members/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;

    public MemberBadgeController(final MemberBadgeService memberBadgeService) {
        this.memberBadgeService = memberBadgeService;
    }

    @GetMapping
    public ResponseEntity<List<MemberBadgeDTO>> getAllMemberBadges() {
        return ResponseEntity.ok(memberBadgeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberBadgeDTO> getMemberBadge(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(memberBadgeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberBadge(
            @RequestBody @Valid final MemberBadgeDTO memberBadgeDTO) {
        final Long createdId = memberBadgeService.create(memberBadgeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberBadge(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MemberBadgeDTO memberBadgeDTO) {
        memberBadgeService.update(id, memberBadgeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberBadge(@PathVariable(name = "id") final Long id) {
        memberBadgeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
