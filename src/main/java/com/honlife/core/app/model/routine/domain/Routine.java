package com.honlife.core.app.model.routine.domain;

import com.honlife.core.app.model.routine.code.RepeatType;
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
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.domain.Member;


@Entity
@Getter
@Setter
public class Routine extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String content;

    @Column
    private String triggerTime;

    @Column
    private Boolean isImportant;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private RepeatType repeatType = RepeatType.DAILY;

    @Column(length = 100)
    private String repeatValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
