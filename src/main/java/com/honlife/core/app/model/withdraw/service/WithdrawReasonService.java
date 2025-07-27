package com.honlife.core.app.model.withdraw.service;

import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.withdraw.domain.WithdrawReason;
import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
import com.honlife.core.app.model.withdraw.repos.WithdrawReasonRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@RequiredArgsConstructor
@Service
public class WithdrawReasonService {

    private final WithdrawReasonRepository withdrawReasonRepository;
    private final ModelMapper mapper;

    public List<WithdrawReasonDTO> findAll() {
        final List<WithdrawReason> withdrawReasons = withdrawReasonRepository.findAll(Sort.by("id"));
        return withdrawReasons.stream()
                .map(withdrawReason -> mapToDTO(withdrawReason, new WithdrawReasonDTO()))
                .toList();
    }

    public WithdrawReasonDTO get(final Long id) {
        return withdrawReasonRepository.findById(id)
                .map(withdrawReason -> mapToDTO(withdrawReason, new WithdrawReasonDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND));
    }

    public Long create(final WithdrawReasonDTO withdrawReasonDTO) {
        final WithdrawReason withdrawReason = new WithdrawReason();
        mapToEntity(withdrawReasonDTO, withdrawReason);
        return withdrawReasonRepository.save(withdrawReason).getId();
    }

    public void update(final Long id, final WithdrawReasonDTO withdrawReasonDTO) {
        final WithdrawReason withdrawReason = withdrawReasonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND));
        mapToEntity(withdrawReasonDTO, withdrawReason);
        withdrawReasonRepository.save(withdrawReason);
    }

    public void delete(final Long id) {
        withdrawReasonRepository.deleteById(id);
    }

    private WithdrawReasonDTO mapToDTO(final WithdrawReason withdrawReason,
            final WithdrawReasonDTO withdrawReasonDTO) {
        withdrawReasonDTO.setId(withdrawReason.getId());
        withdrawReasonDTO.setType(withdrawReason.getType());
        withdrawReasonDTO.setReason(withdrawReason.getReason());
        withdrawReasonDTO.setCreatedAt(withdrawReason.getCreatedAt());
        return withdrawReasonDTO;
    }

    private WithdrawReason mapToEntity(final WithdrawReasonDTO withdrawReasonDTO,
            final WithdrawReason withdrawReason) {
        withdrawReason.setType(withdrawReasonDTO.getType());
        withdrawReason.setReason(withdrawReasonDTO.getReason());
        withdrawReason.setCreatedAt(withdrawReasonDTO.getCreatedAt());
        return withdrawReason;
    }

    /**
     * 입력한 두 날짜 사이의 탈퇴 사유를 조회합니다.
     * @param pageable 탈퇴사유를 페이지네이션으로 가져오기 위한 페이지네이션 정보
     * @param startDate 조회 시작일(포함)
     * @param endDate 조회 종료일(포함)
     * @return Page<WithdrawReasonDTO>
     */
    public Page<WithdrawReasonDTO> findPagedByDateBetween(Pageable pageable ,LocalDateTime startDate, LocalDateTime endDate) {

        return withdrawReasonRepository.findPagedByDateBetween(pageable, startDate, endDate)
            .map(e-> mapper.map(e, WithdrawReasonDTO.class));

    }
}
