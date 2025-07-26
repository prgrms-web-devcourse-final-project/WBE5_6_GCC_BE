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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.domain.Member;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Routine extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "routine_sequence",
            sequenceName = "routine_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "routine_sequence"
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
    @JoinColumn(name = "category_id")
    private Category category;

    public void updateRoutine(Category category, String content, String triggerTime, Boolean isImportant, RepeatType repeatType, String repeatValue, Member member) {
        this.category = category;
        this.content = content;
        this.triggerTime = triggerTime;
        this.isImportant = isImportant;
        this.repeatType = repeatType;
        this.repeatValue = repeatValue;
        this.member = member;
    }
}
