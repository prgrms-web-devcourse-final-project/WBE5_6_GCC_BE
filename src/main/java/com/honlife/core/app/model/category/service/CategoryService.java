package com.honlife.core.app.model.category.service;

import com.honlife.core.app.controller.category.payload.CategorySaveRequest;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.ChildCategoryDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.category.repos.InterestCategoryRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutinePresetRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final RoutinePresetRepository routinePresetRepository;
    private final BadgeRepository badgeRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final ModelMapper mapper;
    private final MemberService memberService;

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
            .map(category -> mapToDTO(category, new CategoryDTO()))
            .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
            .map(category -> mapToDTO(category, new CategoryDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
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
        categoryDTO.setParent(category.getParent() == null ? null : category.getParent().getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setType(category.getType());
        categoryDTO.setMember(category.getMember() == null ? null : category.getMember().getId());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        category.setIsActive(categoryDTO.getIsActive());
        final Category parentCategory = categoryDTO.getParent() == null ? null : categoryRepository.findById(categoryDTO.getParent())
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_CATEGORY));
        category.setParent(parentCategory);
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        final Member member = categoryDTO.getMember() == null ? null : memberRepository.findById(categoryDTO.getMember())
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        category.setMember(member);
        return category;
    }

    /**
     * 참조 무결성을 점검하고, 경고 메시지를 제공하는 사전 검증용 로직
     * @param id 카테고리 아이디
     * @return {@link ReferencedWarning}
     */
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        final Routine categoryRoutine = routineRepository.findFirstByCategory(category);
        if (categoryRoutine != null) {
            referencedWarning.setKey("category.routine.category.referenced");
            referencedWarning.addParam(categoryRoutine.getId());
            return referencedWarning;
        }
        // 사용자 카테고리만 삭제하기에 아래는 확인하지 않아도 괜찮음
//        final RoutinePreset categoryRoutinePreset = routinePresetRepository.findFirstByCategory(category);
//        if (categoryRoutinePreset != null) {
//            referencedWarning.setKey("category.routinePreset.category.referenced");
//            referencedWarning.addParam(categoryRoutinePreset.getId());
//            return referencedWarning;
//        }
//        final Badge categoryBadge = badgeRepository.findFirstByCategory(category);
//        if (categoryBadge != null) {
//            referencedWarning.setKey("category.badge.category.referenced");
//            referencedWarning.addParam(categoryBadge.getId());
//            return referencedWarning;
//        }
//        final InterestCategory categoryInterestCategory = interestCategoryRepository.findFirstByCategory(category);
//        if (categoryInterestCategory != null) {
//            referencedWarning.setKey("category.interestCategory.category.referenced");
//            referencedWarning.addParam(categoryInterestCategory.getId());
//            return referencedWarning;
//        }
        return null;
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 카테고리를 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropCategoryByMemberId(Long memberId) {
        categoryRepository.softDropByMemberId(memberId);
    }

    /**
     * 사용자 이메일을 받아 기본 카테고리와 하위 카테고리를 반환합니다. 이 때 하위 카테고리는 사용자의 카테고리만 반환됩니다.
     * @param userEmail 사용자 이메일
     * @return {@code List<CategoryDTO>}
     */
    public List<CategoryDTO> getDefaultCategories(String userEmail) {
        return new ArrayList<>(
            categoryRepository.findDefaultCategory(userEmail).stream().map(
                category -> CategoryDTO.builder()
                    .id(category.getId())
                    .children(category.getChildren().stream().map(
                        ChildCategoryDTO::fromEntity
                    ).toList())
                    .name(category.getName())
                    .type(category.getType())
                    .member(category.getMember().getId())
                    .emoji(category.getEmoji())
                    .build()
            ).toList());
    }

    /**
     * 사용자 이메일을 받아 사용자 카테고리와 사용자 하위 카테고리를 반환합니다.
     * @param userEmail 사용자 이메일
     * @return {@code List<CategoryDTO>}
     */
    public List<CategoryDTO> getCustomCategories(String userEmail) {
        return new ArrayList<>(
            categoryRepository.findCustomCategory(userEmail).stream().map(
                category -> CategoryDTO.builder()
                    .id(category.getId())
                    .children(category.getChildren().stream().map(
                        ChildCategoryDTO::fromEntity
                    ).toList())
                    .name(category.getName())
                    .type(category.getType())
                    .member(category.getMember().getId())
                    .emoji(category.getEmoji())
                    .build()
            ).toList());
    }

    /**
     * 사용자 아이디를 받아 기본 카테고리와 커스텀 카테고리 모두를 반환합니다.
     * @param userEmail 사용자 이메일
     * @return {@code List<CategoryDTO>}
     */
    public List<CategoryDTO> getCategories(String userEmail) {

        // 기본 카테고리
        List<CategoryDTO> categories = getDefaultCategories(userEmail);

        // 커스텀 카테고리
        List<CategoryDTO> customCategories = getCustomCategories(userEmail);

        categories.addAll(customCategories);

        return categories;
    }

    /**
     * id를 통해 카테고리 정보를 검색합니다.
     * @param categoryId 카테고리 아이디
     * @param userEmail 유저 이메일
     * @return {@link CategoryDTO}
     */
    public CategoryDTO findCategoryById(Long categoryId, String userEmail) {
        Category category = categoryRepository.findCategoryById(categoryId, userEmail)
            .orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        return CategoryDTO.builder()
            .id(category.getId())
            .children(category.getChildren().stream().map(
                ChildCategoryDTO::fromEntity
            ).toList())
            .name(category.getName())
            .type(category.getType())
            .parent(category.getType()== CategoryType.SUB? category.getParent().getId() : null)
            .member(category.getMember().getId())
            .emoji(category.getEmoji())
            .build();


    }

    /**
     * 카테고리를 생성합니다.
     * @param categorySaveRequest 카테고리 생성 시 필요한 정보
     * @param userEmail 멤버 이메일
     */
    @Transactional
    public void createCategory(CategorySaveRequest categorySaveRequest, String userEmail) {
        // 부모 카테고리 정보 가져오기
        Category majorCategory = null;

        if(categorySaveRequest.getCategoryType() == CategoryType.SUB){
            majorCategory = categoryRepository.findCategoryById(categorySaveRequest.getParentId(), userEmail)
                    .orElseThrow(()-> new NotFoundException(ResponseCode.NOT_FOUND_CATEGORY));
        }

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(()-> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));

        Category category = Category.builder()
            .name(categorySaveRequest.getCategoryName())
            .emoji(categorySaveRequest.getEmoji())
            .type(categorySaveRequest.getCategoryType())
            .parent(majorCategory)
            .member(member)
            .build();

        categoryRepository.save(category);
    }

    /**
     * 카테고리를 업데이트 합니다.
     * @param categoryId 업데이트할 카테고리
     * @param userEmail 유저 이메일
     * @param categorySaveRequest 업데이트할 카테고리 정보
     */
    @Transactional
    public void updateCategory(Long categoryId, String userEmail, CategorySaveRequest categorySaveRequest) {
        // 부모 카테고리 정보 가져오기
        Category majorCategory = null;

        if(categorySaveRequest.getCategoryType()==CategoryType.SUB && categorySaveRequest.getParentId() != null){
            // 커스텀 카테고리에서 찾지 못하면 기본 카테고리에서 찾음
            majorCategory = categoryRepository.findCategoryById(categorySaveRequest.getParentId(), userEmail)
                    .orElseThrow(()-> new NotFoundException(ResponseCode.NOT_FOUND_CATEGORY));
        }

        Category targetCategory = categoryRepository.findCategoryById(categoryId, userEmail)
            .orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        targetCategory.setName(categorySaveRequest.getCategoryName());
        targetCategory.setType(categorySaveRequest.getCategoryType());
        targetCategory.setParent(majorCategory);

        if(categorySaveRequest.getEmoji() != null && !categorySaveRequest.getEmoji().isBlank()){
            targetCategory.setEmoji(categorySaveRequest.getEmoji());
        }
        categoryRepository.save(targetCategory);

    }

    /**
     * 해당 카테고리가 기본 카테고리인지 확인합니다.
     * @param categoryId 해당 카테고리 아이디
     * @return boolean
     */
    public boolean isDefault(Long categoryId) {
        Category targetCategory = categoryRepository.findCategoryById(categoryId).orElseThrow(()-> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        return targetCategory.getType()==CategoryType.DEFAULT;

    }

    /**
     * 아이디를 통해 카테고리를 소프트 드랍합니다.
     * @param categoryId 해당 카테고리 아이디
     */
    @Transactional
    public void softDrop(Long categoryId) {
        Category targetCategory = categoryRepository.findCategoryById(categoryId).orElseThrow(()-> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        if(targetCategory.getType() == CategoryType.MAJOR){
            targetCategory.getChildren().forEach(
                child ->{
                    child.setIsActive(false);
                }
            );
        }

        targetCategory.setIsActive(false);
        categoryRepository.save(targetCategory);
    }
}
