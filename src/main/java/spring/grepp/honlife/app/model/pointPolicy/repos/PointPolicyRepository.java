package spring.grepp.honlife.app.model.pointPolicy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.pointPolicy.domain.PointPolicy;


public interface PointPolicyRepository extends JpaRepository<PointPolicy, Integer> {
}
