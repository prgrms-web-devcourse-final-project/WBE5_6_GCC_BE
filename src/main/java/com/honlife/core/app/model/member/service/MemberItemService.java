package com.honlife.core.app.model.member.service;

import java.util.List;

import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.QItem;
import com.honlife.core.app.model.member.domain.QMemberItem;
import com.honlife.core.app.model.member.repos.MemberItemRepositoryCustom;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.model.MemberItemDTO;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
@RequiredArgsConstructor
public class MemberItemService {

    private final MemberItemRepository memberItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final MemberItemRepositoryCustom memberItemRepositoryCustom;

    /**
     * 특정 회원이 보유한 아이템 목록을 조회합니다.
     * 아이템 타입이 지정된 경우 해당 타입에 해당하는 아이템만 필터링하여 반환합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @param itemType 필터링할 아이템 타입 (null 가능)
     * @return MemberItemResponse 리스트
     */
    public List<MemberItemResponse> getItemsByMember(Long memberId, ItemType itemType) {
        List<Tuple> tuples = memberItemRepositoryCustom.findMemberItems(memberId, itemType);

        return tuples.stream().map(tuple -> {
            MemberItem mi = tuple.get(QMemberItem.memberItem);
            Item item = tuple.get(QItem.item);
            return MemberItemResponse.builder()
                    .itemKey(item.getItemKey())
                    .itemName(item.getName())
                    .itemDescription(item.getDescription())
                    .itemtype(item.getType())
                    .isEquipped(mi.getIsEquipped())
                    .build();
        }).toList();
    }

    /**
     * 특정 회원이 현재 장착 중인 아이템 목록을 조회합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 장착된 MemberItemResponse 리스트
     */
    public List<MemberItemResponse> getEquippedItemsByMember(Long memberId){
        return memberItemRepository.findEquippedItemsByMemberId(memberId);
    }


    public List<MemberItemDTO> findAll() {
        final List<MemberItem> memberItems = memberItemRepository.findAll(Sort.by("id"));
        return memberItems.stream()
                .map(memberItem -> mapToDTO(memberItem, new MemberItemDTO()))
                .toList();
    }

    public MemberItemDTO get(final Long id) {
        return memberItemRepository.findById(id)
                .map(memberItem -> mapToDTO(memberItem, new MemberItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberItemDTO memberItemDTO) {
        final MemberItem memberItem = new MemberItem();
        mapToEntity(memberItemDTO, memberItem);
        return memberItemRepository.save(memberItem).getId();
    }

    public void update(final Long id, final MemberItemDTO memberItemDTO) {
        final MemberItem memberItem = memberItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberItemDTO, memberItem);
        memberItemRepository.save(memberItem);
    }

    public void delete(final Long id) {
        memberItemRepository.deleteById(id);
    }

    private MemberItemDTO mapToDTO(final MemberItem memberItem, final MemberItemDTO memberItemDTO) {
        memberItemDTO.setCreatedAt(memberItem.getCreatedAt());
        memberItemDTO.setUpdatedAt(memberItem.getUpdatedAt());
        memberItemDTO.setIsActive(memberItem.getIsActive());
        memberItemDTO.setId(memberItem.getId());
        memberItemDTO.setIsEquipped(memberItem.getIsEquipped());
        memberItemDTO.setMember(memberItem.getMember() == null ? null : memberItem.getMember().getId());
        memberItemDTO.setItem(memberItem.getItem() == null ? null : memberItem.getItem().getId());
        return memberItemDTO;
    }

    private MemberItem mapToEntity(final MemberItemDTO memberItemDTO, final MemberItem memberItem) {
        memberItem.setCreatedAt(memberItemDTO.getCreatedAt());
        memberItem.setUpdatedAt(memberItemDTO.getUpdatedAt());
        memberItem.setIsActive(memberItemDTO.getIsActive());
        memberItem.setIsEquipped(memberItemDTO.getIsEquipped());
        final Member member = memberItemDTO.getMember() == null ? null : memberRepository.findById(memberItemDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberItem.setMember(member);
        final Item item = memberItemDTO.getItem() == null ? null : itemRepository.findById(memberItemDTO.getItem())
                .orElseThrow(() -> new NotFoundException("item not found"));
        memberItem.setItem(item);
        return memberItem;
    }
}
