package spring.grepp.honlife.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.item.domain.Item;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberItem;


public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

    MemberItem findFirstByMember(Member member);

    MemberItem findFirstByItem(Item item);

}
