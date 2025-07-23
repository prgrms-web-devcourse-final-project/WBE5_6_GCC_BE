package com.honlife.core.app.model.routine.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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


@Entity
@Getter
@Setter
public class RoutinePreset extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "routine_preset_sequence",
            sequenceName = "routine_preset_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "routine_preset_sequence"
    )
    private Long id;

    @Column(length = 50)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column
    private String triggerTime;

    @Column
    private boolean isImportant;



}
