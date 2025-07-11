package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.member.service.MemberItemService;


@Tag(name="회원 보유 아이템", description = "현재 로그인한 회원이 보유하고 있는 아이템 관련 API 입니다.")
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/members/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberItemController {

    private final MemberItemService memberItemService;

    public MemberItemController(final MemberItemService memberItemService) {
        this.memberItemService = memberItemService;
    }

    /**
     * 로그인한 회원이 보유한 아이템 조회
     * @param itemType 아이템 타입을 받아 타입별로 검색 가능
     * @return List<MemberItemResponse>
     */
    @GetMapping
    @Operation(summary = "로그인된 회원의 보유 아이템 조회", description = "로그인된 사용자의 보유 아이템을 조회합니다. <br>type 작성 시 해당 타입에 대한 아이템만 조회됩니다.")
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getMemberItems(
        @Parameter( description = "Item Type (TOP, BOTTOM, ACCESSORY)", example = "TOP")
        @RequestParam(name="type", required = false) String itemType,
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
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }
        if(itemType==null || itemType.isEmpty()) {
            // 전체 조회
            List<MemberItemResponse> response = List.of(top, bottom, accessory);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }else if(itemType.equals(ItemType.TOP.name())) {
            List<MemberItemResponse> response = List.of(top);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }else if(itemType.equals(ItemType.BOTTOM.name())) {
            List<MemberItemResponse> response = List.of(bottom);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }else if(itemType.equals(ItemType.ACCESSORY.name())) {
            List<MemberItemResponse> response = List.of(accessory);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }else{
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
    }

    /**
     * 현재 장착하고 있는 아이템 조회
     * @return List<MemberItemResponse>
     */
    @GetMapping("/equipped")
    @Operation(summary = "로그인된 회원의 현재 장착 아이템 조회", description = "로그인된 사용자의 현재 장착 아이템을 조회합니다.")
    public ResponseEntity<CommonApiResponse<List<MemberItemResponse>>> getEquippedItems(
        @AuthenticationPrincipal UserDetails userDetails
    ){
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
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }
        List<MemberItemResponse> response = List.of(top, accessory);
        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 아이템 장착 상태 변경
     * @param itemKey 아이템 키
     * @return
     */
    @PatchMapping("/{key}")
    @Operation(summary = "아이템 장착 상태 변경", description = "로그인된 사용자의 아이템 장착상태 변경 요청을 처리합니다. <br>"
        + "현재 top_item_01, bottom_item_01이라는 key에 대해서만 OK 를 받을 수 있습니다.")
    public ResponseEntity<CommonApiResponse<Void>> updateItemIsEquipped(
        @PathVariable(name="key") String itemKey,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }

        if (itemKey.equals("top_item_01")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else if (itemKey.equals("bottom_item_01")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
    }
}
