package com.honlife.core.app.model.point.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.point.code.PointSourceType;


@Entity
@Getter
@Setter
public class PointPolicy extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "point_policy_sequence",
            sequenceName = "point_policy_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "point_policy_sequence"
    )
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private PointSourceType type;

    @Column(length = 50)
    private String referenceKey;

    @Column
    private Integer point;

}
