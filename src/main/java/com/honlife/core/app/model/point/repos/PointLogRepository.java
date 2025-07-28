package com.honlife.core.app.model.point.repos;

import com.honlife.core.app.model.point.code.PointLogType;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.point.domain.PointLog;


public interface PointLogRepository extends JpaRepository<PointLog, Long>, PointLogRepositoryCustom{

    PointLog findFirstByMember(Member member);
}
