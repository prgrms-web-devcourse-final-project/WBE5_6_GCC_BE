package spring.grepp.honlife.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.member.domain.MemberImage;


public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
