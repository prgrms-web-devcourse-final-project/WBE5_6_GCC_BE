package spring.grepp.honlife.app.model.point.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.point.domain.PointPolicy;


public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {
}
