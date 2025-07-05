package spring.grepp.honlife.app.model.pointLog.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.pointLog.domain.PointLog;


public interface PointLogRepository extends JpaRepository<PointLog, Integer> {

    PointLog findFirstByMember(Member member);

}
