package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
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

        // 보유 아이템 판단
        List<Long> ownedItemIds = memberItemService.getOwnedItemIdsByMember(userDetails.getUsername());

        List<Item> items = itemService.getAllItems(itemType);

        List<ItemResponse> responseList =  items.stream()
                .map(item -> ItemResponse.builder()
                        .itemId(item.getId())
                        .itemKey(item.getItemKey())
                        .itemName(item.getName())
                        .itemDescription(item.getDescription())
                        .itemType(item.getType())
                        .itemPoint(item.getPrice())
                        .isOwned(ownedItemIds.contains(item.getId()))
                        .build())
                .toList();

        return ResponseEntity.ok(CommonApiResponse.success(responseList));
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

        Optional<Item> itemOptional = itemService.getItemByKey(itemKey);

        Item item = itemOptional.get();

        // 해당 key값에 해당하는 아이템 보유 여부 확인
        Boolean ownedItemId = memberItemService.isItemOwnByMember(userDetails.getUsername(),item.getId());

        ItemResponse itemResponse = ItemResponse.builder()
                .itemId(item.getId())
                .itemKey(item.getItemKey())
                .itemName(item.getName())
                .itemDescription(item.getDescription())
                .itemType(item.getType())
                .itemPoint(item.getPrice())
                .isOwned(ownedItemId)
                .build();

        return ResponseEntity.ok(CommonApiResponse.success(itemResponse));
    }

    /**
     * 아이템 구매 API
     * @param itemKey 아이템 고유 아이다
     **/
    @PostMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> buyItem(
            @PathVariable("key") String itemKey,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 아이템 키값으로 Item 정보 가져옴
        Optional<Item> itemOptional = itemService.getItemByKey(itemKey);

        Item item = itemOptional.get();
        // 아이템에대한 사용자 보유여부 확인
        Boolean ownedItemId = memberItemService.isItemOwnByMember(userDetails.getUsername(),item.getId());

        // 이미 보유 시 이미 보유했다는 응답 반환
        if(ownedItemId){
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_ITEM));
        }
        // 아이템 구매 메서드
        itemService.purchaseItem(item,userDetails.getUsername());
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
