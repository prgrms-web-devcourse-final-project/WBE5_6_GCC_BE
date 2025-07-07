package spring.grepp.honlife.app.model.member.domain;

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
import spring.grepp.honlife.app.model.common.BaseEntity;
import spring.grepp.honlife.app.model.member.code.ResidenceExperience;
import spring.grepp.honlife.app.model.member.code.Role;
import spring.grepp.honlife.app.model.memberImage.domain.MemberImage;
import spring.grepp.honlife.app.model.notification.domain.Notification;


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
    private Integer id;

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

    @Column(nullable = false, length = 10)
    private String gender;

    @Column
    @Enumerated(EnumType.STRING)
    private ResidenceExperience residenceExperience;

    @Column
    private String regionDept1;

    @Column
    private String regionDept2;

    @Column
    private String regionDept3;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_image_id", nullable = false, unique = true)
    private MemberImage memberImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, unique = true)
    private Notification notification;

}
