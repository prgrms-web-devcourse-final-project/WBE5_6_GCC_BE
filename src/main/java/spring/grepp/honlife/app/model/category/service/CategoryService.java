package spring.grepp.honlife.app.model.category.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.badge.domain.Badge;
import spring.grepp.honlife.app.model.badge.repos.BadgeRepository;
import spring.grepp.honlife.app.model.category.domain.Category;
import spring.grepp.honlife.app.model.category.domain.InterestCategory;
import spring.grepp.honlife.app.model.category.dto.CategoryDTO;
import spring.grepp.honlife.app.model.category.repos.CategoryRepository;
import spring.grepp.honlife.app.model.category.repos.InterestCategoryRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.repos.MemberRepository;
import spring.grepp.honlife.app.model.routine.domain.Routine;
import spring.grepp.honlife.app.model.routine.repos.RoutineRepository;
import spring.grepp.honlife.app.model.routine.domain.RoutinePreset;
import spring.grepp.honlife.app.model.routine.repos.RoutinePresetRepository;
import spring.grepp.honlife.infra.util.NotFoundException;
import spring.grepp.honlife.infra.util.ReferencedWarning;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final RoutinePresetRepository routinePresetRepository;
    private final BadgeRepository badgeRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    public CategoryService(final CategoryRepository categoryRepository,
        final MemberRepository memberRepository, final RoutineRepository routineRepository,
        final RoutinePresetRepository routinePresetRepository,
        final BadgeRepository badgeRepository,
        final InterestCategoryRepository interestCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.routineRepository = routineRepository;
        this.routinePresetRepository = routinePresetRepository;
        this.badgeRepository = badgeRepository;
        this.interestCategoryRepository = interestCategoryRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
            .map(category -> mapToDTO(category, new CategoryDTO()))
            .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
            .map(category -> mapToDTO(category, new CategoryDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        categoryDTO.setIsActive(category.getIsActive());
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setType(category.getType());
        categoryDTO.setMember(category.getMember() == null ? null : category.getMember().getId());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        category.setIsActive(categoryDTO.getIsActive());
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        final Member member = categoryDTO.getMember() == null ? null : memberRepository.findById(categoryDTO.getMember())
            .orElseThrow(() -> new NotFoundException("member not found"));
        category.setMember(member);
        return category;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        final Routine categoryRoutine = routineRepository.findFirstByCategory(category);
        if (categoryRoutine != null) {
            referencedWarning.setKey("category.routine.category.referenced");
            referencedWarning.addParam(categoryRoutine.getId());
            return referencedWarning;
        }
        final RoutinePreset categoryRoutinePreset = routinePresetRepository.findFirstByCategory(category);
        if (categoryRoutinePreset != null) {
            referencedWarning.setKey("category.routinePreset.category.referenced");
            referencedWarning.addParam(categoryRoutinePreset.getId());
            return referencedWarning;
        }
        final Badge categoryBadge = badgeRepository.findFirstByCategory(category);
        if (categoryBadge != null) {
            referencedWarning.setKey("category.badge.category.referenced");
            referencedWarning.addParam(categoryBadge.getId());
            return referencedWarning;
        }
        final InterestCategory categoryInterestCategory = interestCategoryRepository.findFirstByCategory(category);
        if (categoryInterestCategory != null) {
            referencedWarning.setKey("category.interestCategory.category.referenced");
            referencedWarning.addParam(categoryInterestCategory.getId());
            return referencedWarning;
        }
        return null;
    }

}
