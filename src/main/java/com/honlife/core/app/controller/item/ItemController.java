package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final MemberItemService memberItemService;

    /**
     * 모든 아이템 또는 Type 일치 아이템 조회 API
     * @return List<ItemResponse> 모든 아이템에 대한 정보
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getAllItems(
            @RequestParam(value = "type", required = false) ItemType itemType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 이메일로 Member 엔티티 조회
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        // 로그인한 회원의 이메일과 요청 파라미터로 전달된 itemType을 기반으로
        // 해당 회원이 보유한 여부(isOwned)를 포함한 아이템 리스트 조회
        List<ItemResponse> responseList = ItemResponse.fromDTOList(itemService.getAllItemsWithOwnership(member.getId(), itemType));
        return ResponseEntity.ok(CommonApiResponse.success(responseList));
    }
    /**
     * 아이템 구매 API
     *
     * @param id 아이템 고유 아이다
     **/
    @PostMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> buyItem(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 아이템 키값으로 Item 정보 가져옴
        Item item = itemService.getItemById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ITEM));

        Member member = memberService.getMemberByEmail(userDetails.getUsername());

        // 회원이 이미 보유한 아이템인지 확인
        if (memberItemService.isItemOwnByMember(member.getId(), id)) {
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_ITEM));
        }
        try {
            // 해당 item 구매 메서드
            itemService.purchaseItem(item, member);
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } catch (CommonException e) {
            return ResponseEntity.status(ResponseCode.NOT_ENOUGH_POINT.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_ENOUGH_POINT));
        }

    }
}
