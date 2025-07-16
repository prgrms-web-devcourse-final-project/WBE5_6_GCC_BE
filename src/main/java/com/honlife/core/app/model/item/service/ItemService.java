package com.honlife.core.app.model.item.service;

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
     * 아이템 전체 조회 기능
     * itemType 값이 존재 시 그 Type의 아이템 전체 조회
     * @param itemType
     * @return List<Item>
     */
    public List<Item> getAllItems(ItemType itemType) {
        if (itemType != null) {
            return itemRepository.findByTypeAndIsActiveTrue(itemType);
        } else {
            return itemRepository.findAllByIsActiveTrue();
        }
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
     * @param email 구매하는 사용자 email 값
     */
    public void purchaseItem(Item item,String email) {

        Member member = memberService.getMemberByEmail(email);

        // 사용자 email을 활용하여 MemberPoint 정보 가져오는 메서드
        Optional<MemberPoint> pointOptional = memberPointService.getPointByEmail(email);
        MemberPoint point = pointOptional.get();

        if (point.getPoint() < item.getPrice()) {
            throw new CommonException(ResponseCode.NOT_ENOUGH_POINT);
        }
        point.setPoint(point.getPoint()-item.getPrice());


        MemberItem memberItem =MemberItem.builder()
                .member(member)
                .item(item)
                .isEquipped(false) // 기본값
                .build();

        // memberPoint 테이블에 Point 변경값 저장
        memberPointRepository.save(point);
        // 구매 아이템 정보 저장
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
