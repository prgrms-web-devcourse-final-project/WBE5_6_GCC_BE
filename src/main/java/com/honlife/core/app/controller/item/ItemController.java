package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final MemberPointService memberPointService;
    private final MemberItemService memberItemService;

    /**
     * 모든 아이템 또는 Type 일치 아이템 조회 API
     *
     * @return List<ItemResponse> 모든 아이템에 대한 정보
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getAllItems(
            @RequestParam(value = "type", required = false) ItemType itemType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        // 로그인한 회원의 이메일과 요청 파라미터로 전달된 itemType을 기반으로
        // 해당 회원이 보유한 여부(isOwned)를 포함한 아이템 리스트 조회
        // 이메일로 Member 엔티티 조회
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        List<ItemResponse> items = itemService.getAllItemsWithOwnership(member.getId(), itemType);
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 아이템 key값을 통한 단건 조회 API
     *
     * @param itemKey 아이템 고유 아이다
     * @return ItemResponse itemKey 값과 일치하는 아이템 정보 반환
     */
    @GetMapping("/{key}")
    public ResponseEntity<CommonApiResponse<ItemResponse>> getItemByKey(
            @PathVariable("key") String itemKey,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 아이템 조회

        Item item = itemService.getItemByKey(itemKey);
        if (item == null) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        // 사용자 정보 조회
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        ItemResponse itemResponse = itemService.getItemResponseByKey(itemKey, member.getId());

        return ResponseEntity.ok(CommonApiResponse.success(itemResponse));

    }

    /**
     * 아이템 구매 API
     *
     * @param itemKey 아이템 고유 아이다
     **/
    @PostMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> buyItem(
            @PathVariable("key") String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 아이템 키값으로 Item 정보 가져옴
        Item item = itemService.getItemByKey(itemKey);
        if (item == null) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        Member member = memberService.getMemberByEmail(userDetails.getUsername());

        if (memberItemService.isItemOwnByMember(member.getId(), item.getId())) {
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_ITEM));
        }
        try {
            itemService.purchaseItem(item, member);
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } catch (CommonException e) {
            return ResponseEntity.status(ResponseCode.NOT_ENOUGH_POINT.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_ENOUGH_POINT));
        }

    }
}
