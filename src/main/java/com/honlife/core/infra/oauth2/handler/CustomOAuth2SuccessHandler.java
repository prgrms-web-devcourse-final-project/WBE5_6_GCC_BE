package com.honlife.core.infra.oauth2.handler;

import com.honlife.core.app.model.auth.AuthService;
import com.honlife.core.app.model.auth.code.AuthToken;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.loginLog.service.LoginLogService;
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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

        // OAuth2User 로 캐스팅 후 인증된 사용자 정보를 가져온다.
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        // 사용자 이메일을 가져온다.
        String email = user.getAttribute("email");
        // 서비스 제공 플랫폼(GOOGLE, KAKAO)이 어디인지 가져온다.
        String provider = user.getAttribute("provider");

        OAuth2UserInfo userInfo = OAuth2UserInfo.createUserInfo(request.getRequestURI(), user);
        log.info("onAuthenticationSuccess :: New OAuth2 Login request --- emaile = {}, user_name = {}", userInfo.getEmail(), user.getName());

        // 신규회원일때만 추가정보 입력 페이지로 리다이렉트 하기위한 추가 주소 변수
        String targetPath = "/?social=true";

        Member member = memberRepository.findByEmailIgnoreCase(userInfo.getEmail());
        if (member == null) {
            member = Member.builder()
                .email(email)
                .name(userInfo.getName())
                .nickname("USER_" + UUID.randomUUID())
                .isVerified(true)
                .role(Role.ROLE_USER)
                .build();
            memberRepository.save(member);
            targetPath = "/signup?social=true";
            log.info("onAuthenticationSuccess :: Saved new member --- email = {}", member.getEmail());
        }

        SocialAccount socialAccount = socialAccountRepository.findByMemberAndProvider(member,
            userInfo.getProvider()).orElse(null);
        if (socialAccount == null) {
            socialAccount = SocialAccount.builder()
                .provider(provider)
                .providerId(userInfo.getProviderId())
                .member(member)
                .build();
            socialAccountRepository.save(socialAccount);
            log.info("onAuthenticationSuccess :: Saved new social account --- memberId = {}, provider = {}", member.getId(), socialAccount.getProvider());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String roles =  String.join(",", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        TokenDto tokenDto = authService.processTokenSignin(userInfo.getEmail(), roles);

        ResponseCookie accessTokenCookie = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            tokenDto.getAccessToken(), tokenDto.getExpiresIn(), appDomain);

        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            AuthToken.REFRESH_TOKEN.name(),
            tokenDto.getRefreshToken(), tokenDto.getExpiresIn(), appDomain);

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // 로그인시 자동으로 로그인 기록 저장
        loginLogService.newLog(userInfo.getEmail());

        getRedirectStrategy().sendRedirect(request, response, frontDomain + targetPath);
    }
}
