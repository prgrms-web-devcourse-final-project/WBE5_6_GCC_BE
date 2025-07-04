package spring.grepp.honlife.app.model.pointPolicy.domain;

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
import spring.grepp.honlife.app.model.common.BaseEntity;
import spring.grepp.honlife.app.model.pointPolicy.code.PointSourceType;


@Entity
@Getter
@Setter
public class PointPolicy extends BaseEntity {

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
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private PointSourceType type;

    @Column(length = 50)
    private String referenceKey;

    @Column
    private Integer point;

}
