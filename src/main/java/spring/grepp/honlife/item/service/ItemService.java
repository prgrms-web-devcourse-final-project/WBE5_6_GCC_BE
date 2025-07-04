package spring.grepp.honlife.item.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.item.domain.Item;
import spring.grepp.honlife.item.model.ItemDTO;
import spring.grepp.honlife.item.repos.ItemRepository;
import spring.grepp.honlife.member_item.domain.MemberItem;
import spring.grepp.honlife.member_item.repos.MemberItemRepository;
import spring.grepp.honlife.util.NotFoundException;
import spring.grepp.honlife.util.ReferencedWarning;


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

    public ItemDTO get(final Integer id) {
        return itemRepository.findById(id)
                .map(item -> mapToDTO(item, new ItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ItemDTO itemDTO) {
        final Item item = new Item();
        mapToEntity(itemDTO, item);
        return itemRepository.save(item).getId();
    }

    public void update(final Integer id, final ItemDTO itemDTO) {
        final Item item = itemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(itemDTO, item);
        itemRepository.save(item);
    }

    public void delete(final Integer id) {
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

    public ReferencedWarning getReferencedWarning(final Integer id) {
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
