package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.code.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.item.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * IsActiveTrue인 Item 값을 전체 조회합니다.
     * @return List<Item></Item>
     */
    List<Item> findAllByIsActiveTrue();

    /**
     * ItemType의 정보로 IsActive가 True 인 값을 조회합니다.
     * @param itemType 아이템 Type
     * @return List<Item></Item>
     */
    List<Item> findByTypeAndIsActiveTrue(ItemType itemType);

    /**
     * ItemKey의 정보로 IsActive가 True 인 값을 조회합니다.
     * @param itemKey item에 대한 key 값
     * @return Optional<Item>
     */
    Optional<Item> findByItemKeyAndIsActiveTrue(String itemKey);

    // ItemKey 값의 Unique함을 보장하기 위함
    boolean existsByItemKeyIgnoreCase(String itemKey);
}
