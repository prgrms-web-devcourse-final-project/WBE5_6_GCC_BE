package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * ItemKey의 정보로 IsActive가 True 인 값을 조회합니다.
     * @param itemKey item에 대한 key 값
     * @return Optional<Item>
     */
    Optional<Item> findByItemKeyAndIsActiveTrue(String itemKey);

    // ItemKey 값의 Unique함을 보장하기 위함
    boolean existsByItemKeyIgnoreCase(String itemKey);

}
