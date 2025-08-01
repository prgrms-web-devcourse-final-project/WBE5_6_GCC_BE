package com.honlife.core.infra.oauth2.handler;

import com.honlife.core.app.model.auth.AuthService;
import com.honlife.core.app.model.auth.code.AuthToken;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.oauth2.domain.SocialAccount;
import com.honlife.core.app.model.oauth2.dto.OAuth2UserInfo;
import com.honlife.core.app.model.oauth2.repos.SocialAccountRepository;
import com.honlife.core.infra.auth.jwt.JwtTokenProvider;
import com.honlife.core.infra.auth.jwt.TokenCookieFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        OAuth2UserInfo userInfo = OAuth2UserInfo.createUserInfo(request.getRequestURI(), user);

        Member member = memberRepository.findByEmailIgnoreCase(userInfo.getEmail());
        if (member == null) {
            member = Member.builder()
                .email(user.getName())
                .build();
            memberRepository.save(member);
        }

        SocialAccount socialAccount = socialAccountRepository.findByMemberAndProvider(member,
            userInfo.getProvider()).orElse(null);
        if (socialAccount == null) {
            socialAccount = SocialAccount.builder()
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .build();
            socialAccountRepository.save(socialAccount);
        }

        String roles = String.join(",", authentication.getAuthorities().stream().map(
            GrantedAuthority::getAuthority).toList());

        TokenDto tokenDto = authService.processTokenSignin(userInfo.getName(), roles);
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            tokenDto.getAccessToken(), jwtTokenProvider.getAccessTokenExpiration());

        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            AuthToken.REFRESH_TOKEN.name(),
            tokenDto.getRefreshToken(), tokenDto.getRefreshExpiresIn());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
