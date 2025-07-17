package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.item.domain.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Item findByItemKeyAndIsActiveTrue(String itemKey);

    // ItemKey 값의 Unique함을 보장하기 위함
    boolean existsByItemKeyIgnoreCase(String itemKey);

    /**
     * 사용자(memberId)가 보유한 아이템인지 여부를 포함하여,
     * isActive=true 조건에 해당하는 전체 아이템 목록을 조회합니다.
     *
     * @param memberId 사용자 ID
     * @param itemType 아이템 타입 (null일 경우 전체)
     * @return ItemResponse 리스트 (각 아이템에 대해 isOwned 필드 포함)
     */
    @Query("SELECT new com.honlife.core.app.controller.item.payload.ItemResponse(" +
            "i.id, i.itemKey, i.name, i.description, i.type, i.price, " +
            "CASE WHEN mi.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Item i " +
            "LEFT JOIN MemberItem mi ON i.id = mi.item.id AND mi.member.id = :memberId " +
            "WHERE i.isActive = true AND (:itemType IS NULL OR i.type = :itemType)")
    List<ItemResponse> findItemsWithOwnership(@Param("memberId") Long memberId, @Param("itemType") ItemType itemType);
}
