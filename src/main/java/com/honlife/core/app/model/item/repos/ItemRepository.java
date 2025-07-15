package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.code.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.item.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByIsActiveTrue();

    List<Item> findByTypeAndIsActiveTrue(ItemType itemType);

    Optional<Item> findByItemKeyAndIsActiveTrue(String itemKey);

    boolean existsByItemKeyIgnoreCase(String itemKey);
}
