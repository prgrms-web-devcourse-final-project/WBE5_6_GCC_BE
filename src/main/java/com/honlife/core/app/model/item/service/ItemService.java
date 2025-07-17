package com.honlife.core.app.model.item.service;

import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.domain.QItem;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepositoryCustom;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.domain.QMemberItem;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberPointRepository memberPointRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberService memberService;
    private final MemberPointService memberPointService;
    private final ItemRepositoryCustom itemRepositoryCustom;

    /**
     * 특정 사용자의 아이템 전체 목록을 조회하면서,
     * 해당 사용자가 각 아이템을 보유하고 있는지를 함께 판단하여 응답합니다.
     *
     * @param memberId 사용자 이메일 (UserDetails.getUsername())
     * @param itemType 아이템 타입 (null일 경우 전체 아이템 조회)
     * @return ItemResponse 리스트 (isOwned 필드 포함)
     */
    public List<ItemDTO> getAllItemsWithOwnership(Long memberId, ItemType itemType) {

        List<Tuple> tuples = itemRepositoryCustom.findItemsWithOwnership(memberId, itemType);

        return tuples.stream()
                .map(tuple -> {
                    Item i = tuple.get(QItem.item);
                    Boolean isOwned = tuple.get(QMemberItem.memberItem.id) != null;

                    return ItemDTO.builder()
                            .id(i.getId())
                            .itemKey(i.getItemKey())
                            .name(i.getName())
                            .description(i.getDescription())
                            .type(i.getType())
                            .price(i.getPrice())
                            .isOwned(isOwned)
                            .build();
                })
                .toList();
    }

    /**
     * 회원 ID와 아이템 key를 기준으로 해당 아이템 정보를 조회하고,
     * 회원의 보유 여부를 포함한 ItemResponse DTO로 반환합니다.
     *
     * @param itemKey   조회할 아이템의 고유 키
     * @param memberId  현재 로그인한 회원의 ID
     * @return          아이템 정보 및 보유 여부를 담은 ItemResponse
     */
    public ItemDTO getItemResponseByKey(String itemKey, Long memberId) {
        Tuple tuple = itemRepositoryCustom.findItemWithOwnership(itemKey, memberId);
        if (tuple == null) {
            throw new CommonException(ResponseCode.NOT_FOUND_ITEM);
        }

        Item i = tuple.get(QItem.item);
        Boolean isOwned = tuple.get(QMemberItem.memberItem.id) != null;

        return ItemDTO.builder()
                .id(i.getId())
                .itemKey(i.getItemKey())
                .name(i.getName())
                .description(i.getDescription())
                .type(i.getType())
                .price(i.getPrice())
                .isOwned(isOwned)
                .build();
    }


    /**
     * itemKey로 단일 아이템 조회
     * @param itemKey
     * return Optional<Item></Item>
     */
    public Item getItemByKey(String itemKey) {
        return itemRepository.findByItemKeyAndIsActiveTrue(itemKey);
    }

    /**
     * 아이템 구매 기능
     * @param item 컨트롤러에서 key값을 통해 구매하려는 Item 정보를 가지고 있음
     * @param member 구매하는 사용자 Member 테이블 값
     */
    @Transactional
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
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ITEM));
        final MemberItem itemMemberItem = memberItemRepository.findFirstByItem(item);
        if (itemMemberItem != null) {
            referencedWarning.setKey("item.memberItem.item.referenced");
            referencedWarning.addParam(itemMemberItem.getId());
            return referencedWarning;
        }
        return null;
    }
}
