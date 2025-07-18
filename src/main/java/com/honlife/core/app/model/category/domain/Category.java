package com.honlife.core.app.model.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.domain.Member;


@Entity
@Getter
@Setter
public class Category extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "category_sequence",
        sequenceName = "category_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    private Long id;

    @Column
    private Long parentId;

    @Column(length = 25)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column
    private String emoji;

}
