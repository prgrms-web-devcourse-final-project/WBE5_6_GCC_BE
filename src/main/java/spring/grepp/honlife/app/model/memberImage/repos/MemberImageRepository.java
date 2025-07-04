package spring.grepp.honlife.app.model.memberImage.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.memberImage.domain.MemberImage;


public interface MemberImageRepository extends JpaRepository<MemberImage, Integer> {
}
