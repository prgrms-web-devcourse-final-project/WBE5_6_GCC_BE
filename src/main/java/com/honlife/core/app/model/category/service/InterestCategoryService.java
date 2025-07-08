package com.honlife.core.app.model.category.service;

import java.util.List;
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

}
