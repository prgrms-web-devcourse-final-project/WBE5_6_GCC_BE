package com.honlife.core.app.model.item.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.item.domain.Item;


public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByItemKeyIgnoreCase(String itemKey);

}
