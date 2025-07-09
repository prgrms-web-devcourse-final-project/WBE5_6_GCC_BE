package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.playload.BuyItemPayload;
import com.honlife.core.app.controller.item.playload.ItemPayload;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "아이템", description = "아이템 관련 api 입니다.")
@RestController
@RequestMapping(value = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }


    /*
     * 모든 아이템 조회 API
     * @return List<ItemPayload> 모든 아이템에 대한 정보
     * */
    @GetMapping
    @Operation(summary = "아이템 정보 전체 조회", description = "상점에 있는 아이템 정보 전체를 조회합니다.")
    public ResponseEntity<CommonApiResponse<List<ItemPayload>>> getAllItems() {
        List<ItemPayload> items = new ArrayList<>();
        items.add(ItemPayload.builder()
                .itemId(1L)
                .type("모자")
                .itemKey("head_item_01")
                .itemName("청소 모자")
                .itemPoint(100)
                .build());
        items.add(ItemPayload.builder()
                .itemId(2L)
                .type("신발")
                .itemKey("shoes_item_01")
                .itemName("러닝 신발")
                .itemPoint(100)
                .build());
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 아이템 구매 API
     *
     * @param itemId 아이템 고유 아이다
     * @return BuyItemPayload 사용자가 구매한 아이템 정보 반환
     */
    @Operation(summary = "아이템 구매", description = "포인트를 차감하고 아이템을 구매합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "아이템 구매 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "아이템 구매 후 100포인트 차감되었습니다.",
                  "data": {
                    "memberId": 123,
                    "itemId": 1,
                    "createdAt": "2025-07-09T10:30:00"
                  }
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "포인트 부족",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "포인트가 부족합니다.",
                  "data": null
                }
            """)
                    )
            )
    })
    @PostMapping("/{id}")
    public ResponseEntity<CommonApiResponse<BuyItemPayload>> buyItem(
            @Parameter(name = "id", description = "구매할 아이템의 ID", example = "1")
            @PathVariable("id") Long itemId) {
        BuyItemPayload buyItem= BuyItemPayload.builder()
                .memberId(123L)
                .itemId(itemId)
                .createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(CommonApiResponse.success(buyItem));

    }
}
