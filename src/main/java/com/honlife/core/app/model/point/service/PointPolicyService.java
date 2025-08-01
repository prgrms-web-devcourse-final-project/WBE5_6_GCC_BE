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

    /**
     * referenceKey로 포인트 정책 조회
     * @param referenceKey 참조 키 (배지 키 등)
     * @return 포인트 정책 DTO
     */
    public PointPolicyDTO findByReferenceKey(String referenceKey) {
        return pointPolicyRepository.findByReferenceKeyAndIsActiveTrue(referenceKey)
            .map(policy -> mapToDTO(policy, new PointPolicyDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POLICY));
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
