package com.honlife.core.app.model.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.code.ResidenceExperience;
import com.honlife.core.app.model.notification.domain.Notification;
import lombok.ToString;


@Entity
@Getter
@Setter
public class Member extends BaseEntity {

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column
    @Enumerated(EnumType.STRING)
    private ResidenceExperience residenceExperience;

    @Column
    private String region1Dept;

    @Column
    private String region2Dept;

    @Column
    private String region3Dept;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_image_id", nullable = false, unique = true)
    private MemberImage memberImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, unique = true)
    private Notification notification;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private MemberPoint member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_point_id", nullable = false, unique = true)
    private MemberPoint memberPoint;
}
