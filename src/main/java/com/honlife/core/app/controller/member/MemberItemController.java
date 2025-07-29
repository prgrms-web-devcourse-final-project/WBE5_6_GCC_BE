package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberItemController {

    private final MemberItemService memberItemService;
    private final MemberService memberService;

    /**
     * 로그인한 사용자가 보유한 아이템 전체 조회 (타입 필터 가능)
     *
     * @param itemType    필터링할 아이템 타입 (nullable)
     * @param userDetails 인증된 사용자 정보
     * @return CommonApiResponse<List < MemberItemResponse>>
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getMemberItems(
            @RequestParam(name = "type", required = false) ItemType itemType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        // 회원이 보유한 아이템 리스트 반환, itemType 값이 존재 시 해당 Type에 대한 리스트만 반환
        List<MemberItemResponse> responseList = MemberItemResponse.fromDTOList(memberItemService.getItemsByMember(member.getId(), itemType));
        return ResponseEntity.ok(CommonApiResponse.success(responseList));
    }

    /**
     * 로그인된 사용자가 아이템 장착 상태 변경 API
     * @param id 장착 또는 해제하려는 아이템의 고유 id
     * @param userDetails   로그인된 사용자 정보
     */
    @PatchMapping()
    public ResponseEntity<CommonApiResponse<Void>> SwitchEquipItem(
            @RequestParam Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        memberItemService.switchItemEquip(member.getId(), id);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
