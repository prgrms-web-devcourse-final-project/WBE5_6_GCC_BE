package com.honlife.core.app.model.item.service;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberPointRepository memberPointRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberService memberService;
    private final MemberPointService memberPointService;

    /**
     * 특정 사용자의 아이템 전체 목록을 조회하면서,
     * 해당 사용자가 각 아이템을 보유하고 있는지를 함께 판단하여 응답합니다.
     *
     * @param username 사용자 이메일 (UserDetails.getUsername())
     * @param itemType 아이템 타입 (null일 경우 전체 아이템 조회)
     * @return ItemResponse 리스트 (isOwned 필드 포함)
     */
    public List<ItemResponse> getAllItemsWithOwnership(String username, ItemType itemType) {
        // 이메일로 Member 엔티티 조회
        Member member = memberService.getMemberByEmail(username);
        // 해당 사용자의 memberId와 itemType을 기준으로 아이템 리스트 조회 (isOwned 포함)
        return itemRepository.findItemsWithOwnership(member.getId(), itemType);
    }

    /**
     * itemKey로 단일 아이템 조회
     * @param itemKey
     * return Optional<Item></Item>
     */
    public Optional<Item> getItemByKey(String itemKey) {
        return itemRepository.findByItemKeyAndIsActiveTrue(itemKey);
    }

    /**
     * 아이템 구매 기능
     * @param item 컨트롤러에서 key값을 통해 구매하려는 Item 정보를 가지고 있음
     * @param member 구매하는 사용자 Member 테이블 값
     */
    public void purchaseItem(Item item,Member member) {

        MemberPoint point = memberPointService.getPointByMemberId(member.getId())
                .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));

        if (point.getPoint() < item.getPrice()) {
            throw new CommonException(ResponseCode.NOT_ENOUGH_POINT);
        }

        point.setPoint(point.getPoint() - item.getPrice());

        MemberItem memberItem = MemberItem.builder()
                .member(member)
                .item(item)
                .isEquipped(false)
                .build();

        memberPointRepository.save(point);
        memberItemRepository.save(memberItem);
    }
    /**
     * itemKeyExists,mapToDTO,mapToEntity,get,getReferencedWarning
     * Item Unique 보장을 위함
     */
    public boolean itemKeyExists(final String itemKey) {
        return itemRepository.existsByItemKeyIgnoreCase(itemKey);
    }

    private ItemDTO mapToDTO(final Item item, final ItemDTO itemDTO) {
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setUpdatedAt(item.getUpdatedAt());
        itemDTO.setIsActive(item.getIsActive());
        itemDTO.setId(item.getId());
        itemDTO.setItemKey(item.getItemKey());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setType(item.getType());
        return itemDTO;
    }
    private Item mapToEntity(final ItemDTO itemDTO, final Item item) {
        item.setCreatedAt(itemDTO.getCreatedAt());
        item.setUpdatedAt(itemDTO.getUpdatedAt());
        item.setIsActive(itemDTO.getIsActive());
        item.setItemKey(itemDTO.getItemKey());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setType(itemDTO.getType());
        return item;
    }

    public ItemDTO get(final Long id) {
        return itemRepository.findById(id)
                .map(item -> mapToDTO(item, new ItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Item item = itemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final MemberItem itemMemberItem = memberItemRepository.findFirstByItem(item);
        if (itemMemberItem != null) {
            referencedWarning.setKey("item.memberItem.item.referenced");
            referencedWarning.addParam(itemMemberItem.getId());
            return referencedWarning;
        }
        return null;
    }
}
