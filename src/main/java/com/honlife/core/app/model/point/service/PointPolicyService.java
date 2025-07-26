package com.honlife.core.app.model.point.service;

import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.point.domain.PointPolicy;
import com.honlife.core.app.model.point.dto.PointPolicyDTO;
import com.honlife.core.app.model.point.repos.PointPolicyRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class PointPolicyService {

    private final PointPolicyRepository pointPolicyRepository;

    public List<PointPolicyDTO> findAll() {
        final List<PointPolicy> pointPolicies = pointPolicyRepository.findAll(Sort.by("id"));
        return pointPolicies.stream()
                .map(pointPolicy -> mapToDTO(pointPolicy, new PointPolicyDTO()))
                .toList();
    }

    public PointPolicyDTO get(final Long id) {
        return pointPolicyRepository.findById(id)
                .map(pointPolicy -> mapToDTO(pointPolicy, new PointPolicyDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POLICY));
    }

    public Long create(final PointPolicyDTO pointPolicyDTO) {
        final PointPolicy pointPolicy = new PointPolicy();
        mapToEntity(pointPolicyDTO, pointPolicy);
        return pointPolicyRepository.save(pointPolicy).getId();
    }

    public void update(final Long id, final PointPolicyDTO pointPolicyDTO) {
        final PointPolicy pointPolicy = pointPolicyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POLICY));
        mapToEntity(pointPolicyDTO, pointPolicy);
        pointPolicyRepository.save(pointPolicy);
    }

    public void delete(final Long id) {
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

    /**
     * Find amount of point with reference key and point source type
     * @param key reference key
     * @param type point source type
     * @return {@code Integer} amount of point
     */
    public Integer getPoint(String key, PointSourceType type) {
        PointPolicy pointPolicy = pointPolicyRepository.findByTypeAndReferenceKeyAndIsActive(type,
                key, true)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POLICY));
        return pointPolicy.getPoint();
    }
}
