package com.honlife.core.app.model.point.repos;

import com.honlife.core.app.model.point.code.PointSourceType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.point.domain.PointPolicy;


public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {

    Optional<PointPolicy> findByTypeAndReferenceKeyAndIsActive(PointSourceType type, String referenceKey, Boolean isActive);
}
