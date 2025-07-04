package spring.grepp.honlife.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import spring.grepp.honlife.app.model.member.code.Role;


@Getter
@Setter
public class MemberDTO {

    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Role role;

    @NotNull
    @Size(max = 50)
    @MemberEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 10)
    private String name;

    @NotNull
    @Size(max = 50)
    @MemberNicknameUnique
    private String nickname;

    @NotNull
    @Size(max = 10)
    private String gender;

    @Size(max = 255)
    private String regionDept1;

    @Size(max = 255)
    private String regionDept2;

    @Size(max = 255)
    private String regionDept3;

    @NotNull
    @MemberMemberImageUnique
    private Integer memberImage;

    @NotNull
    @MemberNotificationUnique
    private Integer notification;

}
