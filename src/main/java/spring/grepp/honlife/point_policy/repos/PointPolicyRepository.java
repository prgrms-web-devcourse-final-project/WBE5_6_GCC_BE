package spring.grepp.honlife.point_policy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.point_policy.domain.PointPolicy;


public interface PointPolicyRepository extends JpaRepository<PointPolicy, Integer> {
}
