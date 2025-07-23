package com.honlife.core.app.model.point.repos;

import com.honlife.core.app.model.point.code.PointSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.point.domain.PointPolicy;
import org.springframework.stereotype.Repository;


@Repository
public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {

  PointPolicy findByType(PointSourceType routine);
}
