package com.honlife.core.app.model.member.domain;

import com.honlife.core.infra.oauth2.domain.SocialAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.code.ResidenceExperience;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "member_sequence",
        sequenceName = "member_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "member_sequence"
    )
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, length = 50)
    private String email;

    @Column
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, length = 50)
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

    @Builder.Default
    @Column(nullable = false)
    private Boolean isVerified = false; // init value

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<SocialAccount> socialAccount;

}
