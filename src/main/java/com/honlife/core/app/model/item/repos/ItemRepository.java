package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * ItemKey의 정보로 IsActive가 True 인 값을 조회합니다.
     * @param key item에 대한 key 값
     * @return Optional<Item>
     */
    Optional<Item> findByKeyAndIsActiveTrue(String key);

    /**
     * itemId의 정보로 IsActive가 True 인 값을 조회합니다.
     * @param id item에 대한 id 값
     * @return Optional<Item>
     */
    Optional<Item> findByIdAndIsActiveTrue(Long id);
    //Key 값의 Unique함을 보장하기 위함
    boolean existsByKeyIgnoreCase(String key);

}
