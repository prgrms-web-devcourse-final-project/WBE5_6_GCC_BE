package com.honlife.core.app.model.point.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.point.domain.PointLog;


public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    PointLog findFirstByMember(Member member);

}
