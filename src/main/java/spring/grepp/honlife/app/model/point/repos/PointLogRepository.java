package spring.grepp.honlife.app.model.point.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.point.domain.PointLog;


public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    PointLog findFirstByMember(Member member);

}
