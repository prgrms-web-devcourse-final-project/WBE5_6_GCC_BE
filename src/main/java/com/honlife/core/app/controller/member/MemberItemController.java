package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberItemEquippedRequest;
import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberItemController {

    private final MemberItemService memberItemService;
    private final MemberService memberService;

    /**
     * 로그인한 회원이 보유한 아이템 조회
     * @param itemType 아이템 타입을 받아 타입별로 검색 가능
     * @return List<MemberItemResponse>
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getMemberItems(
        @RequestParam(name="type", required = false) ItemType itemType,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // email 기반 member 조회 (memberId 얻기 위함)
        // email 기반 member 조회 (memberId 얻기 위함)
        Optional<Member> memberOptional = memberService.getByEmail(userDetails.getUsername());
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }
        // 여기서 memberId 얻을 수 있음
        Member member = memberOptional.get();
        // 사용자 ID 꺼내기
        Long memberId = member.getId();
        // 보유 아이템 판단
        List<Long> ownedItemIds = memberItemService.getOwnedItemIdsByMember(memberId);
        return null;
    }

    /**
     * 현재 장착하고 있는 아이템 조회
     * @return List<MemberItemResponse>
     */
    @GetMapping("/equipped")
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getEquippedItems(
        @AuthenticationPrincipal UserDetails userDetails
    ){
        return null;
    }

    /**
     * 로그인된 사용자의 아이템 장착 상태를 변경하는 API입니다.
     * 이 API는 사용자의 아이템 장착 여부를 토대로 다음 세 가지 동작을 수행합니다:
     *   아이템 바꿔 착용: oldItemKey와 newItemKey가 서로 다른 경우
     *   아이템 벗기: oldItemKey만 넘어오고 newItemKey는 비어있는 경우
     *   착용한 아이템이 없던 곳에 아이템 착용하기: oldItemKey는 비어 있고 newItemKey만 전달된 경우
     *
     * @param equippedRequest 장착하고 있던 아이템과 새로 장착할 아이템 정보를 담은 요청 객체
     * @param userDetails 인증된 사용자 정보
     * @return 유효하지 않은 사용자 요청 또는 존재하지 않는 아이템 키에 대해서는 <code>UNAUTHORIZED (401)</code> 또는 <code>NOT_FOUND_ITEM (404)</code> 상태 코드를 반환합니다.
     */
    @PatchMapping
    @Operation(summary = "아이템 장착 상태 변경",
        description = "로그인된 사용자의 아이템 장착상태 변경 요청을 처리합니다. <br>"+
             "<strong>~사용 시나리오~</strong><br>" +
             "현재 장착하고 있는 item은 top_item_01뿐인 상태<br>" +
             "새로 장착 가능한 itemd은 top_item_02, bottom_item_01 입니다.<br>"+
             "• 아이템 바꿔 착용하기 : oldItemKey와 newItemKey가 서로 다른 아이템<br>" +
             "• 아이템 벗기 : oldItemKey만 넘어오고 newItemKey는 빈 문자열<br>" +
             "• 착용한 아이템이 없던 곳에 아이템 착용하기 : oldItemKey가 빈 문자열, newItemKey만 넘어옴 <br>" +
             "*실제 DB에 반영되지 않음*")
    public ResponseEntity<CommonApiResponse<Void>> updateItemIsEquipped(
        @RequestBody MemberItemEquippedRequest equippedRequest,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
                .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
        }


        if (equippedRequest.getOldItemKey().equals("top_item_01")) {
            // 아이템 벗기
            if(equippedRequest.getNewItemKey().isBlank()) {
                return ResponseEntity.ok(CommonApiResponse.noContent());
            }
            // 아이템 바꿔 착용
            return ResponseEntity.ok(CommonApiResponse.noContent());

        } else if (equippedRequest.getOldItemKey().isBlank() && equippedRequest.getNewItemKey().equals("bottom_item_01")) {
            // 장착 안 하고 있다가 장착하기
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
    }
}
