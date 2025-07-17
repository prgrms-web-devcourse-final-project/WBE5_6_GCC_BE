package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.member.service.MemberItemService;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberItemController {

    private final MemberItemService memberItemService;
    private final ItemService itemService;
    private final MemberService memberService;

    /**
     * 로그인한 회원이 보유한 아이템 조회
     *
     * @param itemType 아이템 타입을 받아 타입별로 검색 가능
     * @return List<MemberItemResponse>
     */
    @GetMapping
    @Operation(summary = "로그인된 회원의 보유 아이템 조회", description = "로그인된 사용자의 보유 아이템을 조회합니다. <br>type 작성 시 해당 타입에 대한 아이템만 조회됩니다.")
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getMemberItems(
            @Parameter(description = "아이템 타입", example = "TOP")
            @RequestParam(name = "type", required = false) ItemType itemType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 임시 데이터
        MemberItemResponse top = MemberItemResponse.builder()
                .itemKey("top_item_01")
                .itemName("청소 상의")
                .itemtype(ItemType.TOP)
                .isEquipped(true)
                .build();
        MemberItemResponse bottom = MemberItemResponse.builder()
                .itemKey("bottom_item_01")
                .itemName("청바지")
                .itemtype(ItemType.BOTTOM)
                .isEquipped(false)
                .build();
        MemberItemResponse accessory = MemberItemResponse.builder()
                .itemKey("accessory_item_01")
                .itemName("요리사 모자")
                .itemtype(ItemType.ACCESSORY)
                .isEquipped(true)
                .build();


        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
                    .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
        }
        if (itemType == null) {
            // 전체 조회
            List<MemberItemResponse> response = List.of(top, bottom, accessory);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        } else if (itemType.equals(ItemType.TOP)) {
            List<MemberItemResponse> response = List.of(top);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        } else if (itemType.equals(ItemType.BOTTOM)) {
            List<MemberItemResponse> response = List.of(bottom);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        } else if (itemType.equals(ItemType.ACCESSORY)) {
            List<MemberItemResponse> response = List.of(accessory);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        } else {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                    .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
    }

    /**
     * 현재 장착하고 있는 아이템 조회
     *
     * @return List<MemberItemResponse>
     */
    @GetMapping("/equipped")
    @Operation(summary = "로그인된 회원의 현재 장착 아이템 조회", description = "로그인된 사용자의 현재 장착 아이템을 조회합니다.")
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getEquippedItems(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 임시 데이터
        MemberItemResponse top = MemberItemResponse.builder()
                .itemKey("top_item_01")
                .itemName("청소 상의")
                .itemtype(ItemType.TOP)
                .isEquipped(true)
                .build();
        MemberItemResponse accessory = MemberItemResponse.builder()
                .itemKey("accessory_item_01")
                .itemName("요리사 모자")
                .itemtype(ItemType.ACCESSORY)
                .isEquipped(true)
                .build();

        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
                    .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
        }
        List<MemberItemResponse> response = List.of(top, accessory);
        return ResponseEntity.ok(CommonApiResponse.success(response));
    }


    @PatchMapping("/equip")
    public ResponseEntity<CommonApiResponse<Void>> equipItem(
            @RequestParam String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 아이템 키값으로 Item 정보 가져옴
        Item item = itemService.getItemByKey(itemKey);
        if (item == null) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        memberItemService.equipItem(member.getId(),item.getId());

        return null;
    }

    @PatchMapping("/unequip")
    public ResponseEntity<CommonApiResponse<Void>> unequipItem(
            @RequestParam String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 아이템 키값으로 Item 정보 가져옴
        Item item = itemService.getItemByKey(itemKey);
        if (item == null) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        memberItemService.unequipItemByItemId(member.getId(),item.getId());
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
