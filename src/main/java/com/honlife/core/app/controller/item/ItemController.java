package com.honlife.core.app.controller.item;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
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
    /**
     * 모든 아이템 또는 Type 일치 아이템 조회 API
     *
     * @return List<ItemResponse> 모든 아이템에 대한 정보
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<ItemResponse>>> getAllItems(
            @RequestParam(value = "type", required = false) ItemType itemType
    ) {


        List<Item> items = itemService.getAllItems(itemType);
        List<ItemResponse> responseList =  items.stream()
                .map(item -> ItemResponse.builder()
                        .itemId(item.getId())
                        .itemKey(item.getItemKey())
                        .itemName(item.getName())
                        .itemType(item.getType())
                        .itemPoint(item.getPrice())
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
            @PathVariable("key") String itemKey) {

        Optional<Item> itemOptional = itemService.getItemByKey(itemKey);

        // ItemKey 값의 존재 여부 확인
        if (itemOptional.isEmpty()) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }

        Item item = itemOptional.get();
        ItemResponse itemResponse = ItemResponse.builder()
                .itemId(item.getId())
                .itemKey(item.getItemKey())
                .itemName(item.getName())
                .itemType(item.getType())
                .itemPoint(item.getPrice())
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
        Optional<Item> itemOptional = itemService.getItemByKey(itemKey);

        // ItemKey 값의 존재 여부 확인
        if (itemOptional.isEmpty()) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_ITEM.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }

        // email 기반 member 조회 (memberId 얻기 위함)
        Optional<Member> memberOptional = memberService.getByEmail(userDetails.getUsername());
        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }

        Item item = itemOptional.get();
        Member member = memberOptional.get(); // 여기서 memberId 얻을 수 있음
        Long memberId = member.getId();

        Optional<MemberPoint> pointOptional = memberPointService.getByMemberId(memberId);
        // memberPoint에 point 값이 있는지 없는지 검증로직을 거치지 않아서 표시되는 warning
        MemberPoint memberPoint = pointOptional.get();

        //  포인트 부족 검사
        if (memberPoint.getPoint() < item.getPrice()) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                    .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        itemService.purchaseItem(item, member);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
