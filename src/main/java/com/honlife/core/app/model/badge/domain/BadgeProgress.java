package com.honlife.core.app.model.badge.domain;

import com.honlife.core.app.model.badge.code.CountType;
import com.honlife.core.app.model.badge.code.ProgressType;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.domain.Member;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "badge_progress",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "progress_type", "progress_key"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeProgress extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "badge_progress_sequence",
        sequenceName = "badge_progress_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "badge_progress_sequence"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "progress_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgressType progressType;

    @Column(name = "progress_key", length = 20, nullable = false)
    private String progressKey;

    @Column(name = "count_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CountType countType;

    @Column(name = "count_value", nullable = false)
    private Integer countValue;

    @Column(name = "last_date")
    private LocalDate lastDate;

}
