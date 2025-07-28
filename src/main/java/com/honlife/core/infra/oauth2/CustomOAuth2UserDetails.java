package com.honlife.core.infra.oauth2;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.infra.oauth2.dto.OAuth2UserInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserDetails implements OAuth2User, UserDetails {

    @Getter
    private final Member member;
    private final Map<String, Object> attributes;

    // 최초 회원가입 or 기존 회원 분기 지정용
    @Getter
    private final Boolean isNewMember;

    public CustomOAuth2UserDetails(Member member, Map<String, Object> attributes, Boolean isNewMember) {
        this.member = member;
        this.attributes = attributes;
        this.isNewMember = isNewMember;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }
}
