package com.honlife.core.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.MemberPoint;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {
}
