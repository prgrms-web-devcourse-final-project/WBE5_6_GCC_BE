package com.honlife.core.app.model.badge.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.dto.BadgeDTO;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;


@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberBadgeRepository memberBadgeRepository;

    public BadgeService(final BadgeRepository badgeRepository,
        final CategoryRepository categoryRepository,
        final MemberBadgeRepository memberBadgeRepository) {
        this.badgeRepository = badgeRepository;
        this.categoryRepository = categoryRepository;
        this.memberBadgeRepository = memberBadgeRepository;
    }

    public List<BadgeDTO> findAll() {
        final List<Badge> badges = badgeRepository.findAll(Sort.by("id"));
        return badges.stream()
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .toList();
    }

    public BadgeDTO get(final Long id) {
        return badgeRepository.findById(id)
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final BadgeDTO badgeDTO) {
        final Badge badge = new Badge();
        mapToEntity(badgeDTO, badge);
        return badgeRepository.save(badge).getId();
    }

    public void update(final Long id, final BadgeDTO badgeDTO) {
        final Badge badge = badgeRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(badgeDTO, badge);
        badgeRepository.save(badge);
    }

    public void delete(final Long id) {
        badgeRepository.deleteById(id);
    }

    private BadgeDTO mapToDTO(final Badge badge, final BadgeDTO badgeDTO) {
        badgeDTO.setCreatedAt(badge.getCreatedAt());
        badgeDTO.setUpdatedAt(badge.getUpdatedAt());
        badgeDTO.setIsActive(badge.getIsActive());
        badgeDTO.setId(badge.getId());
        badgeDTO.setKey(badge.getKey());
        badgeDTO.setName(badge.getName());
        badgeDTO.setTier(badge.getTier());
        badgeDTO.setHow(badge.getHow());
        badgeDTO.setRequirement(badge.getRequirement());
        badgeDTO.setInfo(badge.getInfo());
        badgeDTO.setCategory(badge.getCategory() == null ? null : badge.getCategory().getId());
        return badgeDTO;
    }

    private Badge mapToEntity(final BadgeDTO badgeDTO, final Badge badge) {
        badge.setCreatedAt(badgeDTO.getCreatedAt());
        badge.setUpdatedAt(badgeDTO.getUpdatedAt());
        badge.setIsActive(badgeDTO.getIsActive());
        badge.setKey(badgeDTO.getKey());
        badge.setName(badgeDTO.getName());
        badge.setTier(badgeDTO.getTier());
        badge.setHow(badgeDTO.getHow());
        badge.setRequirement(badgeDTO.getRequirement());
        badge.setInfo(badgeDTO.getInfo());
        final Category category = badgeDTO.getCategory() == null ? null : categoryRepository.findById(badgeDTO.getCategory())
            .orElseThrow(() -> new NotFoundException("category not found"));
        badge.setCategory(category);
        return badge;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Badge badge = badgeRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        final MemberBadge badgeMemberBadge = memberBadgeRepository.findFirstByBadge(badge);
        if (badgeMemberBadge != null) {
            referencedWarning.setKey("badge.memberBadge.badge.referenced");
            referencedWarning.addParam(badgeMemberBadge.getId());
            return referencedWarning;
        }
        return null;
    }

}
