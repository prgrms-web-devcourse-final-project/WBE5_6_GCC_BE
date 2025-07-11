package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import com.honlife.core.infra.util.ReferencedException;
import com.honlife.core.infra.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@SecurityRequirement(name = "bearerAuth")
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
     * @return List<ItemResponse> 모든 아이템에 대한 정보
     * */
    @GetMapping
    @Operation(summary = "아이템 정보 전체 조회", description = "상점에 있는 아이템 정보 전체를 조회합니다.")
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getAllItems() {
        List<ItemResponse> items = new ArrayList<>();
        items.add(ItemResponse.builder()
                .itemId(1L)
                .itemType("모자")
                .itemKey("head_item_01")
                .itemName("청소 모자")
                .itemPoint(100)
                .build());
        items.add(ItemResponse.builder()
                .itemId(2L)
                .itemType("신발")
                .itemKey("shoes_item_01")
                .itemName("러닝 신발")
                .itemPoint(100)
                .build());
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 아이템 타입을 통한 단건 조회 API
     *
     *
     * @return List<ItemResponse> 타입이 일치하는 아이템 조회
     */
    @Operation(summary = "타입 일치 아이템 조회", description = "아이템 type 값을 통해 특정 아이템을 조회합니다.")
    @Schema(name = "itemType",description = "아이템 타입을 적어주세요" ,example = "모자")
    @GetMapping("/by-type")
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getItemByType(
            @Schema(name = "itemType",description = "아이템 타입을 적어주세요" ,example = "모자")
            @RequestParam("itemType") String itemType) {
        List<ItemResponse> items = new ArrayList<>();
        if(!itemType.equals("모자")){
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM_TYPE.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM_TYPE));
        }
        items.add(ItemResponse.builder()
                .itemId(1L)
                .itemType("모자")
                .itemKey("head_item_01")
                .itemName("청소 모자")
                .itemPoint(100)
                .build());
        items.add(ItemResponse.builder()
                .itemId(2L)
                .itemType("모자")
                .itemKey("head_item_02")
                .itemName("요리 모자")
                .itemPoint(101)
                .build());
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 아이템 key값을 통한 단건 조회 API
     *
     * @param itemKey 아이템 고유 아이다
     * @return ItemResponse itenKey 값과 일치하는 아이템 정보 반환
     */
    @Operation(summary = "아이템 단건 조회", description = "아이템 key 값을 통해 특정 아이템을 조회합니다.")
    @GetMapping("/by-key")
    public ResponseEntity<CommonApiResponse<ItemResponse>> getItemByKey(
            @Schema(name = "itemKey",description = "아이템 Key 값을 적어주세요" ,example = "head_item_01")
            @RequestParam("itemKey") String itemKey) {


        if(!itemKey.equals("head_item_01")){
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM_KEY.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM_KEY));
        }
        ItemResponse item = ItemResponse.builder()
                .itemId(1L)
                .itemType("모자")
                .itemKey("head_item_01")
                .itemName("청소 모자")
                .itemPoint(100)
                .build();

        return ResponseEntity.ok(CommonApiResponse.success(item));
    }



    /**
     * 아이템 구매 API
     * @param itemId 아이템 고유 아이다
     */
    @Operation(summary = "아이템 구매", description = "포인트를 차감하고 아이템을 구매합니다.")
    @PostMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> getItem(
            @Parameter(name = "id", description = "구매할 아이템의 ID", example = "1")
            @PathVariable("id") Long itemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        if(!memberId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }
        if(itemId != 1L && itemId != 2L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        if(memberId.equals("user01@test.com") && itemId == 2L ){
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_ITEM));
        }
            return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createItem(@RequestBody @Valid final ItemDTO itemDTO) {
        final Long createdId = itemService.create(itemDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateItem(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final ItemDTO itemDTO) {
        itemService.update(id, itemDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteItem(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = itemService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
