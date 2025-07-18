package com.honlife.core.app.model.badge.domain;

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
import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.common.BaseEntity;


@Entity
@Getter
@Setter
public class Badge extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "badge_sequence",
        sequenceName = "badge_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "badge_sequence"
    )
    private Long id;

    @Column(length = 50)
    private String key;

    @Column(length = 100)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private BadgeTier tier;

    @Column(length = 100)
    private String how;

    @Column
    private Integer requirement;

    @Column(length = 100)
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
