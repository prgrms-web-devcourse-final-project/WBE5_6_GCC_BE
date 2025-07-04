package spring.grepp.honlife.member_item.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.item.domain.Item;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member_item.domain.MemberItem;


public interface MemberItemRepository extends JpaRepository<MemberItem, Integer> {

    MemberItem findFirstByMember(Member member);

    MemberItem findFirstByItem(Item item);

}
