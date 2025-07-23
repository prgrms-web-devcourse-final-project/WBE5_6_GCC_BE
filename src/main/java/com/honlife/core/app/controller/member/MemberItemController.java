package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "[회원] 아이템", description = "현재 로그인한 회원이 보유하고 있는 아이템 관련 API 입니다.")
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/v1/members/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberItemController {

    private final MemberItemService memberItemService;

    public MemberItemController(final MemberItemService memberItemService) {
        this.memberItemService = memberItemService;
    }

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
                .itemDescription("먼지가 달라 붙지 않아요!")
                .itemtype(ItemType.TOP)
                .isEquipped(true)
                .build();
        MemberItemResponse bottom = MemberItemResponse.builder()
                .itemKey("bottom_item_01")
                .itemName("청바지")
                .itemDescription("패션 감각 +1.")
                .itemtype(ItemType.BOTTOM)
                .isEquipped(false)
                .build();
        MemberItemResponse accessory = MemberItemResponse.builder()
                .itemKey("accessory_item_01")
                .itemName("요리사 모자")
                .itemDescription("위생에 한층 더 강력해집니다.")
                .itemtype(ItemType.ACCESSORY)
                .isEquipped(true)
                .build();

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
        } else {
            List<MemberItemResponse> response = List.of(accessory);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
    }

    /**
     * 로그인된 사용자의 아이템 장착하는 API입니다.
     * @param itemKey 장착하고자 하는 itemKey
     * @param userDetails 인증된 사용자 정보
     * @return 존재하지 않은 또는 회원이 보유하지 않은 itemKey 대해서는 <code>NOT_FOUND_ITEM</code> 상태 코드를 반환합니다.
     */
    @PatchMapping("/equip")
    @Operation(summary = "아이템 장착",
            description = "사용자가 itemKey값에 따른 해당 아이템을 장착합니다. <br>" +
                    "• 아이템 착용하기 : ItemKey가 해당하는 Type의 아이템들은 장착 해제가 되고 해당 아이템만 장착<br>")
    public ResponseEntity<CommonApiResponse<Void>> equipItem(
            @RequestParam String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        if (itemKey.equals("top_item_01")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
    }

    /**
     * 로그인된 사용자의 아이템을 장착해제 하는 API입니다.
     * @param itemKey 장착하고자 하는 itemKey
     * @param userDetails 인증된 사용자 정보
     * @return 존재하지 않은 또는 회원이 보유하지 않은 itemKey 대해서는 <code>NOT_FOUND_ITEM</code> 상태 코드를 반환합니다.
     */
    @PatchMapping("/unequip")
    @Operation(summary = "아이템 장착 해제",
            description = "사용자가 itemKey값에 따른 해당 아이템을 해제. <br>" +
                    "• 아이템 해제하기 : ItemKey값에 해당하는 아이템 장착 해제<br>")
    public ResponseEntity<CommonApiResponse<Void>> unequipItem(
            @RequestParam String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (itemKey.equals("top_item_01")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
    }
}
