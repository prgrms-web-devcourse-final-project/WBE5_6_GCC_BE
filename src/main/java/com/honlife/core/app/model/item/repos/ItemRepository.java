package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * ItemKey의 정보로 IsActive가 True 인 값을 조회합니다.
     * @param itemKey item에 대한 key 값
     * @return Optional<Item>
     */
    Item findByItemKeyAndIsActiveTrue(String itemKey);

    // ItemKey 값의 Unique함을 보장하기 위함
    boolean existsByItemKeyIgnoreCase(String itemKey);

    // Item 테이블의 모든 데이터를 조회합니다. (isActive 조건 없음)
    List<Item> findAllByIsActiveTrue();

}
