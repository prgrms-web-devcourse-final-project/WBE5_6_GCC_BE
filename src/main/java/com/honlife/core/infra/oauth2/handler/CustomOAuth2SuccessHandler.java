package com.honlife.core.infra.oauth2.handler;

import com.honlife.core.app.model.auth.AuthService;
import com.honlife.core.app.model.auth.code.AuthToken;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.loginLog.service.LoginLogService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.oauth2.CustomOAuth2UserDetails;
import com.honlife.core.infra.oauth2.repos.SocialAccountRepository;
import com.honlife.core.infra.auth.jwt.JwtTokenProvider;
import com.honlife.core.infra.auth.jwt.TokenCookieFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    private final LoginLogService loginLogService;

    @Value("${front-server.prod-domain}")
    private String frontDomain;

    @Value("${app.domain-only}")
    private String appDomain;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        // 1. Authentication 객체에서 사용자 정보 가져오기
        CustomOAuth2UserDetails userDetails = (CustomOAuth2UserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();
        boolean isNewMember = userDetails.getIsNewMember();

        // 2. 리다이렉트 경로 설정
        String targetPath = isNewMember ? "/signup?social=true" : "/home?social=true";
        log.info("onAuthenticationSuccess() :: targetPath = {}", targetPath);

        // 3. 토큰 발급 및 쿠키 설정
        String roles =  String.join(",", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        TokenDto tokenDto = authService.processTokenSignin(member.getEmail(), roles);

        ResponseCookie accessTokenCookie = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            tokenDto.getAccessToken(), tokenDto.getExpiresIn(), appDomain);

        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            AuthToken.REFRESH_TOKEN.name(),
            tokenDto.getRefreshToken(), tokenDto.getExpiresIn(), appDomain);

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        log.info("onAuthenticationSuccess() :: Create token and set cookie complete");

        // 로그인시 자동으로 로그인 기록 저장
        loginLogService.newLog(member.getEmail());

        getRedirectStrategy().sendRedirect(request, response, frontDomain + targetPath);
    }
}
