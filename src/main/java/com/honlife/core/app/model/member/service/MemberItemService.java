package com.honlife.core.app.model.member.service;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.domain.QItem;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.item.service.ItemService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.domain.QMemberItem;
import com.honlife.core.app.model.member.model.MemberItemDTO;
import com.honlife.core.app.model.member.model.MemberItemDTOCustom;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.response.ResponseCode;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberItemService {

    private final MemberItemRepository memberItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

     /** 사용자의 아이템 장착 상태를 전환합니다.
      - 클릭한 아이템이 이미 장착 중이라면 장착을 해제합니다.
      - 장착 중인 같은 타입 아이템이 존재하면 해당 아이템을 해제합니다.
      - 이후 클릭한 아이템을 장착 처리합니다.

      @param memberId 사용자 ID
     * @param key  장착 또는 해제할 아이템의 고유 키
     */
    @Transactional
    public void switchItemEquip(Long memberId, String key) {

        Item item = itemService.getItemByKey(key)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ITEM));

        // 해당 itemId과 일치하는 회원 보유 아이템 정보 가져옴
        MemberItem target = memberItemRepository.findByMemberIdAndItemId(memberId, item.getId())
                // 회원이 해당 아이템을 보유하지 않았을 때
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 해당 아이템이 장착되어있었다면
        // 즉, 아이템 해제하기 클릭 시 해제
        if (target.getIsEquipped()) {
            target.setIsEquipped(false);
            return;
        }
        // 해당 멤버의 같은 '타입 및 장착중인 ' 아이템 정보 가져오기
        Optional<MemberItem> currentlyEquippedItem = memberItemRepository
                .findByMemberIdAndItemTypeAndIsEquippedTrue(memberId, item.getType());

        currentlyEquippedItem.ifPresent(equipped -> equipped.setIsEquipped(false));

        target.setIsEquipped(true);
    }

    /**
     * 특정 회원이 보유한 아이템 목록을 조회합니다.
     * 아이템 타입이 지정된 경우 해당 타입에 해당하는 아이템만 필터링하여 반환합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @param itemType 필터링할 아이템 타입 (null 가능)
     * @return MemberItemResponse 리스트
     */
    public List<MemberItemDTOCustom> getItemsByMember(Long memberId, ItemType itemType) {
        List<Tuple> tuples = memberItemRepository.findMemberItems(memberId, itemType);

        return tuples.stream().map(tuple -> {
            MemberItem mi = tuple.get(QMemberItem.memberItem);
            Item item = tuple.get(QItem.item);
            return MemberItemDTOCustom.builder()
                    .itemKey(item.getKey())
                    .itemName(item.getName())
                    .itemDescription(item.getDescription())
                    .itemtype(item.getType())
                    .isEquipped(mi.getIsEquipped())
                    .build();
        }).toList();
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
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ITEM));
    }

    public Long create(final MemberItemDTO memberItemDTO) {
        final MemberItem memberItem = new MemberItem();
        mapToEntity(memberItemDTO, memberItem);
        return memberItemRepository.save(memberItem).getId();
    }

    public void update(final Long id, final MemberItemDTO memberItemDTO) {
        final MemberItem memberItem = memberItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ITEM));
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
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        memberItem.setMember(member);
        final Item item = memberItemDTO.getItem() == null ? null : itemRepository.findById(memberItemDTO.getItem())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ITEM));
        memberItem.setItem(item);
        return memberItem;
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 멤버 아이템을 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropMemberItemByMemberId(Long memberId) {
        memberItemRepository.softDropByMemberId(memberId);
    }

    /**
     * 멤버와 연관된 활성화된 첫 번째 멤버 아이템을 조회합니다.
     * @param member 멤버
     * @param isActive 활성화 상태
     * @return {@link Category}
     */
    public MemberItem findFirstMemberItemByMemberAndIsActive(Member member, boolean isActive) {
        return memberItemRepository.findFirstByMemberAndIsActive(member, isActive);

    }

    /**
     * memberId와 itemId를 통해 해당 아이템을 보유 중인지 여부 반환
     * @param memberId 사용자 ID
     * @param itemId   아이템 ID
     * @return 보유 여부 (true: 보유 중, false: 미보유)
     */
    public Boolean isItemOwnByMember(Long memberId, Long itemId) {
        return memberItemRepository.existsByMemberIdAndItemId(memberId, itemId);
    }

    /**
     * 특정 아이템을 보유한 모든 MemberItem 정보를 조회합니다.
     *
     * @param item 조회할 대상 아이템
     * @return MemberItem 리스트
     */
    public List<MemberItem> findAllByItem(Item item) {
        return memberItemRepository.findAllByItem(item);
    }
}
