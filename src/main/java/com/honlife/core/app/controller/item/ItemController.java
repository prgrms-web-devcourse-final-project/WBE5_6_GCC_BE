package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.service.ItemService;
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

import java.util.ArrayList;
import java.util.List;


@SecurityRequirement(name = "bearerAuth")
@Tag(name = "✅ [일반] 아이템", description = "아이템 관련 api 입니다.")
@RestController
@RequestMapping(value = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 모든 아이템 또는 Type 일치 아이템 조회 API
     * - 회원이 해당 아이템을 가지고있는지에 대한 여부 포함
     * - 아이템 자체가 활성화 되었는지 여부 포함
     * @return List<ItemResponse> 모든 아이템에 대한 정보
     */
    @GetMapping
    @Operation(summary = "✅아이템 조회", description = "전체 아이템 조회 또는 type 값을 통해 특정 아이템만 조회할 수 있습니다.")
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getAllItems(
            @Parameter(description = "조회할 아이템 타입", example = "TOP")
            @RequestParam(value = "type", required = false) ItemType itemType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<ItemResponse> items = new ArrayList<>();
        if (itemType != null) {
            if (itemType == ItemType.TOP) {
                items.add(ItemResponse.builder()
                        .itemId(1L)
                        .itemType(ItemType.TOP)
                        .itemKey("top_item_01")
                        .itemName("청소 상의")
                        .itemDescription("먼지가 달라 붙지 않아요!")
                        .itemPoint(100)
                        .isOwned(true)
                        .isListed(true)
                        .build());
                items.add(ItemResponse.builder()
                        .itemId(2L)
                        .itemType(ItemType.TOP)
                        .itemKey("top_item_02")
                        .itemName("요리 상의")
                        .itemDescription("요리 속도가 +1 증가합니다.")
                        .itemPoint(101)
                        .isOwned(false)
                        .isListed(true)
                        .build());
            }

        } else {
            items.add(ItemResponse.builder()
                    .itemId(1L)
                    .itemType(ItemType.TOP)
                    .itemKey("top_item_01")
                    .itemName("청소 상의")
                    .itemDescription("먼지가 달라 붙지 않아요!")
                    .itemPoint(100)
                    .isOwned(true)
                    .isListed(true)
                    .build());
            items.add(ItemResponse.builder()
                    .itemId(2L)
                    .itemType(ItemType.BOTTOM)
                    .itemKey("bottom_item_01")
                    .itemName("러닝 바지")
                    .itemDescription("달리기 속도가 +1 증가합니다.")
                    .itemPoint(100)
                    .isOwned(false)
                    .isListed(true)
                    .build());
        }
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 아이템 key값을 통한 단건 조회 API
     * - 회원이 보유 중인 아이템인지 여부 포함
     * - 해당 아이템이 활성화/비활성화 여부 포함
     *
     * @param id 아이템 고유 아이다
     * @return ItemResponse id 값과 일치하는 아이템 정보 반환
     */
    @Operation(summary = "✅아이템 단건 조회", description = "아이템 key 값을 통해 특정 아이템을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<ItemResponse>> getItemById(
            @Parameter(description = "아이템 id 값", example = "1L")
            @PathVariable("id") Long id) {

        ItemResponse item = ItemResponse.builder()
                .itemId(1L)
                .itemType(ItemType.TOP)
                .itemKey("top_item_01")
                .itemName("청소 상의")
                .itemDescription("먼지가 달라 붙지 않아요!")
                .itemPoint(100)
                .isOwned(true)
                .isListed(true)
                .build();

        return ResponseEntity.ok(CommonApiResponse.success(item));
    }


    /**
     * 아이템 구매 API
     *
     * @param id 아이템 고유 아이다
     */
    @Operation(summary = "✅아이템 구매", description = "포인트를 차감하고 아이템을 구매합니다.")
    @PostMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> getItem(
            @Parameter(name = "id", description = "구매할 아이템 Id 에 해당하는 아이템을 구매합니다.. <br>" +
                    "<strong>~구매 시나리오~</strong><br>" +
                    "1. 현재 존재하는 itemId = 1L,2L <br>" +
                    "2. 존재하지 않는 Id의 아이템 구매 시 아이템 존재 하지 않는다는 오류 반환<br>" +
                    "3. 회원이 보유중인 아이템 id = 2L.<br>" +
                    "이미 보유중인 아이템이라면 이미보유중이라는 오류메세지 반환" +
                    "이 외의 경우 아이템 정상 구매", example = "1L")
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        if (!memberId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }
        if (!(id==1L) && !(id== 2L)) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        if (memberId.equals("user01@test.com") && (id==2L)) {
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_ITEM));
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
