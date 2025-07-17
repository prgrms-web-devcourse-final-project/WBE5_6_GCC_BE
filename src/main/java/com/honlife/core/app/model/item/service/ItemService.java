package com.honlife.core.app.model.item.service;

import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;


@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberItemRepository memberItemRepository;

    public ItemService(final ItemRepository itemRepository,
        final MemberItemRepository memberItemRepository) {
        this.itemRepository = itemRepository;
        this.memberItemRepository = memberItemRepository;
    }

    public List<ItemDTO> findAll() {
        final List<Item> items = itemRepository.findAll(Sort.by("id"));
        return items.stream()
            .map(item -> mapToDTO(item, new ItemDTO()))
            .toList();
    }

    public ItemDTO get(final Long id) {
        return itemRepository.findById(id)
            .map(item -> mapToDTO(item, new ItemDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ITEM));
    }

    public Long create(final ItemDTO itemDTO) {
        final Item item = new Item();
        mapToEntity(itemDTO, item);
        return itemRepository.save(item).getId();
    }

    public void update(final Long id, final ItemDTO itemDTO) {
        final Item item = itemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ITEM));
        mapToEntity(itemDTO, item);
        itemRepository.save(item);
    }

    public void delete(final Long id) {
        itemRepository.deleteById(id);
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

    public boolean itemKeyExists(final String itemKey) {
        return itemRepository.existsByItemKeyIgnoreCase(itemKey);
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
