package com.honlife.core.app.model.item.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final MemberItemRepository memberItemRepository;

    public List<Item> getAllItems(ItemType itemType) {
        if (itemType != null) {
            return itemRepository.findByTypeAndIsActiveTrue(itemType);
        } else {
            return itemRepository.findAllByIsActiveTrue();
        }
    }
    /**
     * itemKey로 단일 아이템 조회
     */
    public Optional<Item> getItemByKey(String itemKey) {
        return itemRepository.findByItemKeyAndIsActiveTrue(itemKey);
    }
    //    public void purchaseItem(String itemKey, String username) {
//        Optional<Member> member = memberRepository.findByEmail(username)
//
//
//    }




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
