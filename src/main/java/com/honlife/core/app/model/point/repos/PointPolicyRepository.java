package com.honlife.core.app.model.point.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.point.domain.PointPolicy;


public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {
}
