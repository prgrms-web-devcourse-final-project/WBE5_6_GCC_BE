package com.honlife.core.infra.oauth2.handler;

import com.honlife.core.app.model.auth.token.RefreshTokenService;
import com.honlife.core.app.model.loginLog.service.LoginLogService;
import com.honlife.core.app.model.oauth2.domain.SocialAccount;
import com.honlife.core.app.model.oauth2.repos.SocialAccountRepository;
import com.honlife.core.infra.auth.jwt.JwtTokenProvider;
import com.honlife.core.infra.auth.jwt.dto.AccessTokenDto;
import com.honlife.core.infra.oauth2.CustomOAuth2UserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final LoginLogService loginLogService;
    private final SocialAccountRepository socialAccountRepository;

    @Value("${front-server.prod-domain}")
    private String frontDomain;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        // 1. 인증된 사용자 정보 가져오기
        // CustomOAuth2UserDetails 에서 우리 서비스의 memberId (providerId)를 가져옵니다.
        CustomOAuth2UserDetails oAuth2User = (CustomOAuth2UserDetails) authentication.getPrincipal();
        Long memberId = oAuth2User.getMember().getId();
        SocialAccount socialAccount = socialAccountRepository.findByMember_Id(memberId);
        String memberProviderId = socialAccount.getProviderId();

        // 2. JWT Access Token 생성
        String roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        // JwtTokenProvider의 generateAccessToken은 AccessTokenDto를 반환합니다.
        AccessTokenDto accessTokenDto = jwtTokenProvider.generateAccessToken(memberProviderId,
            roles);
        String accessToken = accessTokenDto.getToken();
        String accessTokenJti = accessTokenDto.getJti(); // Access Token의 JTI

        // 3. Refresh Token 생성
        // JwtTokenProvider에 createRefreshToken 메소드를 추가해야 합니다.
        String refreshTokenString = jwtTokenProvider.createRefreshToken(memberProviderId);

        // 4. Refresh Token을 Redis에 저장 (AccessToken의 JTI 를 키로 사용)
        // RefreshTokenService의 saveWithAtId 메소드를 사용합니다.
        // 이 메소드는 RefreshToken 객체를 반환하지만, 여기서는 Redis에 저장하는 것이 목적입니다.
        refreshTokenService.saveWithAtId(
            accessTokenJti); // Access Token의 JTI 를 사용하여 Refresh Token 저장

        // 5. Access Token을 HTTP-only 쿠키에 담기
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) (jwtTokenProvider.getAccessTokenExpiration() / 1000));
        // 프로덕션 환경에서는 반드시 setSecure(true)를 추가해야 합니다.
//        accessTokenCookie.setSecure(true);
        response.addCookie(accessTokenCookie);

        // 6. Refresh Token을 HTTP-only 쿠키에 담기
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshTokenString);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (jwtTokenProvider.getRefreshTokenExpiration() / 1000));
        // 프로덕션 환경에서는 반드시 setSecure(true)를 추가해야 합니다.
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        // 7. 프론트엔드 URL로 리다이렉트
        String targetPath = "";
        if(oAuth2User.getIsNewMember()) targetPath = "/signup?social=true";

        String targetUrl = UriComponentsBuilder.fromUriString(frontDomain + targetPath)
            .build().toUriString();

        loginLogService.newLog(oAuth2User.getUsername());

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }
}
