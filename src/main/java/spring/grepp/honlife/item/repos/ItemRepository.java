package spring.grepp.honlife.item.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.item.domain.Item;


public interface ItemRepository extends JpaRepository<Item, Integer> {
}
