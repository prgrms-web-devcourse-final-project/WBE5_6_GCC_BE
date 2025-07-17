package com.honlife.core.app.model.category.service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.domain.InterestCategory;
import com.honlife.core.app.model.category.dto.InterestCategoryDTO;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.category.repos.InterestCategoryRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class InterestCategoryService {

    private final InterestCategoryRepository interestCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public InterestCategoryService(final InterestCategoryRepository interestCategoryRepository,
            final CategoryRepository categoryRepository, final MemberRepository memberRepository) {
        this.interestCategoryRepository = interestCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    public List<InterestCategoryDTO> findAll() {
        final List<InterestCategory> interestCategories = interestCategoryRepository.findAll(Sort.by("id"));
        return interestCategories.stream()
                .map(interestCategory -> mapToDTO(interestCategory, new InterestCategoryDTO()))
                .toList();
    }

    public InterestCategoryDTO get(final Long id) {
        return interestCategoryRepository.findById(id)
                .map(interestCategory -> mapToDTO(interestCategory, new InterestCategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final InterestCategoryDTO interestCategoryDTO) {
        final InterestCategory interestCategory = new InterestCategory();
        mapToEntity(interestCategoryDTO, interestCategory);
        return interestCategoryRepository.save(interestCategory).getId();
    }

    public void update(final Long id, final InterestCategoryDTO interestCategoryDTO) {
        final InterestCategory interestCategory = interestCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(interestCategoryDTO, interestCategory);
        interestCategoryRepository.save(interestCategory);
    }

    public void delete(final Long id) {
        interestCategoryRepository.deleteById(id);
    }

    private InterestCategoryDTO mapToDTO(final InterestCategory interestCategory,
            final InterestCategoryDTO interestCategoryDTO) {
        interestCategoryDTO.setCreatedAt(interestCategory.getCreatedAt());
        interestCategoryDTO.setUpdatedAt(interestCategory.getUpdatedAt());
        interestCategoryDTO.setIsActive(interestCategory.getIsActive());
        interestCategoryDTO.setId(interestCategory.getId());
        interestCategoryDTO.setCategory(interestCategory.getCategory() == null ? null : interestCategory.getCategory().getId());
        interestCategoryDTO.setMember(interestCategory.getMember() == null ? null : interestCategory.getMember().getId());
        return interestCategoryDTO;
    }

    private InterestCategory mapToEntity(final InterestCategoryDTO interestCategoryDTO,
            final InterestCategory interestCategory) {
        interestCategory.setCreatedAt(interestCategoryDTO.getCreatedAt());
        interestCategory.setUpdatedAt(interestCategoryDTO.getUpdatedAt());
        interestCategory.setIsActive(interestCategoryDTO.getIsActive());
        final Category category = interestCategoryDTO.getCategory() == null ? null : categoryRepository.findById(interestCategoryDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        interestCategory.setCategory(category);
        final Member member = interestCategoryDTO.getMember() == null ? null : memberRepository.findById(interestCategoryDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        interestCategory.setMember(member);
        return interestCategory;
    }

    /**
     * 회원의 관심카테고리 목록을 업데이트<br>
     * 입력받은 카테고리 id 리스트를 기준으로, 기존에 {@code isActive=ture} 인 카테고리들은 유지<br>
     * {@code isActive=false} 인 카테고리들은 true로 갱신<br>
     * 새로운 List에 포함되지 않은 기존 카테고리들을 {@code isActive=false} 로 갱신<br>
     * 새로운 관심 카테고리는 추가
     * @param member 회원 정보 객체
     * @param newCategoryIds 새로 입력받은 관심 카테고리 List
     */
    @Transactional
    public void updateInterestCategory(Member member, List<Long> newCategoryIds) {
        if (newCategoryIds == null || newCategoryIds.isEmpty()) return;

        List<InterestCategory> existingList = interestCategoryRepository.findAllByMember(member);

        Set<Long> newCategoryIdSet = new HashSet<>(newCategoryIds);

        // 기존에 존재하던 관심카테고리 상태 유지 or 변경
        for (InterestCategory interestCategory : existingList) {
            Long categoryId = interestCategory.getCategory().getId();
            interestCategory.setIsActive(newCategoryIdSet.contains(categoryId));
        }

        // 새로운 관심 카테고리 추가
        Set<Long> existingCategoryIds = existingList.stream()
            .map(InterestCategory::getId)
            .collect(Collectors.toSet());

        List<Long> toAdd = newCategoryIds.stream()
            .filter(categoryId -> !existingCategoryIds.contains(categoryId))
            .toList();

        List<Category> categoriesToAdd = categoryRepository.findAllById(toAdd);
        for(Category category : categoriesToAdd) {
            InterestCategory newInterestCategory = new InterestCategory();
            newInterestCategory.setMember(member);
            newInterestCategory.setCategory(category);
            newInterestCategory.setIsActive(true);
            interestCategoryRepository.save(newInterestCategory);
        }
    }


    /**
     * 관심 카테고리 추가
     * @param member 회원 정보 객체
     * @param CategoryIds 관심 카테고리로 등록된 카테고리 List
     */
    @Transactional
    public void saveInterestCategory(Member member, List<Long> CategoryIds) {
        if (CategoryIds == null || CategoryIds.isEmpty()) return;

        for(Category category : categoryRepository.findAllById(CategoryIds)) {
            InterestCategory newInterestCategory = new InterestCategory();
            newInterestCategory.setMember(member);
            newInterestCategory.setCategory(category);
            newInterestCategory.setIsActive(true);
            interestCategoryRepository.save(newInterestCategory);
        }
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 선호 카테고리를 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropInterestCategoryByMemberId(Long memberId) {
        interestCategoryRepository.deleteByMemberId(memberId);
    }

    /**
     * 해당 멤버와 연관된 활성화된 첫번째 선호 카테고리를 조회합니다.
     * @param member 멤버
     * @param isActive 활성화 상태
     * @return {@link InterestCategory}
     */
    public InterestCategory findFirstInterestCategoryByMemberAndIsActive(Member member, boolean isActive) {
        return interestCategoryRepository.findFirstByMemberAndIsActive(member,isActive);
    }
}
