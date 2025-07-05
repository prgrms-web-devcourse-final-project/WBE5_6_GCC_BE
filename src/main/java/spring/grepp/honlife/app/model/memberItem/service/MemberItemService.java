package spring.grepp.honlife.app.model.memberItem.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.item.domain.Item;
import spring.grepp.honlife.app.model.item.repos.ItemRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.repos.MemberRepository;
import spring.grepp.honlife.app.model.memberItem.domain.MemberItem;
import spring.grepp.honlife.app.model.memberItem.dto.MemberItemDTO;
import spring.grepp.honlife.app.model.memberItem.repos.MemberItemRepository;
import spring.grepp.honlife.infra.util.NotFoundException;


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

    public MemberItemDTO get(final Integer id) {
        return memberItemRepository.findById(id)
                .map(memberItem -> mapToDTO(memberItem, new MemberItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MemberItemDTO memberItemDTO) {
        final MemberItem memberItem = new MemberItem();
        mapToEntity(memberItemDTO, memberItem);
        return memberItemRepository.save(memberItem).getId();
    }

    public void update(final Integer id, final MemberItemDTO memberItemDTO) {
        final MemberItem memberItem = memberItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberItemDTO, memberItem);
        memberItemRepository.save(memberItem);
    }

    public void delete(final Integer id) {
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
