package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.service.ItemService;
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



@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService itemService;

    /**
     * 모든 아이템 또는 Type 일치 아이템 조회 API
     * @return List<ItemResponse> 모든 아이템에 대한 정보
     * */
    @GetMapping
    @Operation(summary = "아이템 조회", description = "전체 아이템 조회 또는 type 값을 통해 특정 아이템만 조회할 수 있습니다.")
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getAllItems(
            @RequestParam(value = "type", required = false) ItemType itemType
    ) {
        List<ItemResponse> items = itemService.getAllItems(itemType);

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
            @PathVariable("key") String itemKey) {

        ItemResponse item = itemService.getItemByKey(itemKey);

        return ResponseEntity.ok(CommonApiResponse.success(item));
    }



    /**
     * 아이템 구매 API
     * @param itemId 아이템 고유 아이다
     */
    @PostMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> getItem(
            @PathVariable("key") String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        if(!memberId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }
        if(!itemKey.equals("top_item_01") && !itemKey.equals("top_item_02")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        if(memberId.equals("user01@test.com") && itemKey.equals("top_item_02")){
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_ITEM));
        }
            return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
