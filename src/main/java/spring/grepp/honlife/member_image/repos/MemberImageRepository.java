package spring.grepp.honlife.member_image.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.member_image.domain.MemberImage;


public interface MemberImageRepository extends JpaRepository<MemberImage, Integer> {
}
