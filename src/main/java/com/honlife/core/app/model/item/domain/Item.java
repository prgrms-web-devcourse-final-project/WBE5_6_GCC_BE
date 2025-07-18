package com.honlife.core.app.model.item.domain;

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
import com.honlife.core.app.model.item.code.ItemType;


@Entity
@Getter
@Setter
public class Item extends BaseEntity {

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

    @Column(unique = true, length = 50)
    private String itemKey;

    @Column(length = 50)
    private String name;

    @Column
    private Integer price;

    @Column
    @Enumerated(EnumType.STRING)
    private ItemType type;

}
