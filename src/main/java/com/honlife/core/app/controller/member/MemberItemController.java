package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "✅ [회원] 아이템", description = "현재 로그인한 회원이 보유하고 있는 아이템 관련 API 입니다.")
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
    @Operation(summary = "✅ 로그인된 회원의 보유 아이템 조회", description = "로그인된 사용자의 보유 아이템을 조회합니다. <br>type 작성 시 해당 타입에 대한 아이템만 조회됩니다.")
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
                .isListed(true)
                .build();
        MemberItemResponse bottom = MemberItemResponse.builder()
                .itemKey("bottom_item_01")
                .itemName("청바지")
                .itemDescription("패션 감각 +1.")
                .itemtype(ItemType.BOTTOM)
                .isEquipped(false)
                .isListed(true)
                .build();
        MemberItemResponse accessory = MemberItemResponse.builder()
            .itemKey("accessory_item_01")
            .itemName("요리사 모자")
                .itemDescription("위생에 한층 더 강력해집니다.")
                .itemtype(ItemType.ACCESSORY)
                .isEquipped(true)
                .isListed(true)
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
     * 로그인된 사용자의 아이템 장착 상태를 변경하는 API입니다.
     * 아이템 시나리오에 부연 설명을 하였습니다.
     *
     * @param userDetails     인증된 사용자 정보
     * @return 유효하지 않은 사용자 요청 또는 존재하지 않는 아이템 키에 대해서는 <code>UNAUTHORIZED (401)</code> 또는 <code>NOT_FOUND_ITEM (404)</code> 상태 코드를 반환합니다.
     */
    @PatchMapping
    @Operation(summary = "✅ 아이템 장착 상태 변경",
            description = "로그인된 사용자의 아이템 장착상태 변경 요청을 처리합니다. <br>" +
                    "<strong>~사용 시나리오~</strong><br>" +
                    "1. 아이템 바꿔 착용하기 <br>" +
                    "• 아이템 벗기 : 현재 착용 중이던 아이템은 해제됩니다.<br>" +
                    "• 착용한 아이템이 없던 곳에 아이템 착용하기 : 착용하는 아이템이 장착 됩니다.<br>" +
                    "2. 아이템 해제 하기" +
                    "- 1번을 수행하면 해당 버튼은 착용해제로 바뀌고 눌렀을 경우 장착이 해제 됩니다."+
                    "*실제 DB에 반영되지 않음*")
    public ResponseEntity<CommonApiResponse<Void>> updateItemIsEquipped(
            @Schema(name= "장착하고자 하는 itemKey"
                    , description = "현재 장착 중인 itemKey는 top_item_02"
                    ,example = "top_item_01")
            @RequestParam String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
                    .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
        }

        // 새로운 아이템 장착
        if (itemKey.equals("top_item_01")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        // 장착 해제 으로 장착 해제됨
        if (itemKey.equals("top_item_02")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
    }
}
