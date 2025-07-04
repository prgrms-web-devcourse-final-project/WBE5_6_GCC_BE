package spring.grepp.honlife.point_policy.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.point_policy.domain.PointPolicy;
import spring.grepp.honlife.point_policy.model.PointPolicyDTO;
import spring.grepp.honlife.point_policy.repos.PointPolicyRepository;
import spring.grepp.honlife.util.NotFoundException;


@Service
public class PointPolicyService {

    private final PointPolicyRepository pointPolicyRepository;

    public PointPolicyService(final PointPolicyRepository pointPolicyRepository) {
        this.pointPolicyRepository = pointPolicyRepository;
    }

    public List<PointPolicyDTO> findAll() {
        final List<PointPolicy> pointPolicies = pointPolicyRepository.findAll(Sort.by("id"));
        return pointPolicies.stream()
                .map(pointPolicy -> mapToDTO(pointPolicy, new PointPolicyDTO()))
                .toList();
    }

    public PointPolicyDTO get(final Integer id) {
        return pointPolicyRepository.findById(id)
                .map(pointPolicy -> mapToDTO(pointPolicy, new PointPolicyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PointPolicyDTO pointPolicyDTO) {
        final PointPolicy pointPolicy = new PointPolicy();
        mapToEntity(pointPolicyDTO, pointPolicy);
        return pointPolicyRepository.save(pointPolicy).getId();
    }

    public void update(final Integer id, final PointPolicyDTO pointPolicyDTO) {
        final PointPolicy pointPolicy = pointPolicyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pointPolicyDTO, pointPolicy);
        pointPolicyRepository.save(pointPolicy);
    }

    public void delete(final Integer id) {
        pointPolicyRepository.deleteById(id);
    }

    private PointPolicyDTO mapToDTO(final PointPolicy pointPolicy,
            final PointPolicyDTO pointPolicyDTO) {
        pointPolicyDTO.setCreatedAt(pointPolicy.getCreatedAt());
        pointPolicyDTO.setUpdatedAt(pointPolicy.getUpdatedAt());
        pointPolicyDTO.setIsActive(pointPolicy.getIsActive());
        pointPolicyDTO.setId(pointPolicy.getId());
        pointPolicyDTO.setType(pointPolicy.getType());
        pointPolicyDTO.setReferenceKey(pointPolicy.getReferenceKey());
        pointPolicyDTO.setPoint(pointPolicy.getPoint());
        return pointPolicyDTO;
    }

    private PointPolicy mapToEntity(final PointPolicyDTO pointPolicyDTO,
            final PointPolicy pointPolicy) {
        pointPolicy.setCreatedAt(pointPolicyDTO.getCreatedAt());
        pointPolicy.setUpdatedAt(pointPolicyDTO.getUpdatedAt());
        pointPolicy.setIsActive(pointPolicyDTO.getIsActive());
        pointPolicy.setType(pointPolicyDTO.getType());
        pointPolicy.setReferenceKey(pointPolicyDTO.getReferenceKey());
        pointPolicy.setPoint(pointPolicyDTO.getPoint());
        return pointPolicy;
    }

}
