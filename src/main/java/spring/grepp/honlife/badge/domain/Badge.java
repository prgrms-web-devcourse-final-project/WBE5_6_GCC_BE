package spring.grepp.honlife.badge.domain;

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
import spring.grepp.honlife.badge.model.BadgeTier;
import spring.grepp.honlife.category.domain.Category;
import spring.grepp.honlife.domain.BaseEntity;


@Entity
@Getter
@Setter
public class Badge extends BaseEntity {

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
