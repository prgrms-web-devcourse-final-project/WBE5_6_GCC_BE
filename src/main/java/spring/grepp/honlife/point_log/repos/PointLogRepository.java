package spring.grepp.honlife.point_log.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.point_log.domain.PointLog;


public interface PointLogRepository extends JpaRepository<PointLog, Integer> {

    PointLog findFirstByMember(Member member);

}
