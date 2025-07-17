package com.honlife.core.app.model.withdraw.service;

import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.withdraw.domain.WithdrawReason;
import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
import com.honlife.core.app.model.withdraw.repos.WithdrawReasonRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@Service
public class WithdrawReasonService {

    private final WithdrawReasonRepository withdrawReasonRepository;

    public WithdrawReasonService(final WithdrawReasonRepository withdrawReasonRepository) {
        this.withdrawReasonRepository = withdrawReasonRepository;
    }

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

}
