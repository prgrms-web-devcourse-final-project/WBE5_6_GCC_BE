package com.honlife.core.app.model.member.service;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.model.MemberItemDTO;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberItemService {

    private final MemberItemRepository memberItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public MemberItemService(final MemberItemRepository memberItemRepository,
            final MemberRepository memberRepository, final ItemRepository itemRepository) {
        this.memberItemRepository = memberItemRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
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
