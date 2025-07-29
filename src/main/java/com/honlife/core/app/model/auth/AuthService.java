package com.honlife.core.app.model.auth;

import com.honlife.core.app.controller.auth.payload.LoginRequest;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.auth.token.RefreshTokenService;
import com.honlife.core.app.model.auth.token.UserBlackListRepository;
import com.honlife.core.app.model.auth.token.entity.RefreshToken;
import com.honlife.core.app.model.badge.event.LoginEvent;
import com.honlife.core.app.model.loginLog.service.LoginLogService;
import com.honlife.core.infra.auth.jwt.JwtTokenProvider;
import com.honlife.core.infra.auth.jwt.dto.AccessTokenDto;
import com.honlife.core.infra.event.CommonEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {
    
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserBlackListRepository userBlackListRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDetailsService userDetailsService;
    private final ApplicationEventPublisher eventPublisher;

    private final LoginLogService loginLogService;

    public TokenDto signin(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword());

        // loadUserByUsername + password 검증 후 인증 객체 반환
        // 인증 실패 시: AuthenticationException 발생
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        eventPublisher.publishEvent(
            new CommonEvent(
                loginRequest.getEmail()
            )
        );

        // 배지용 로그인 완료 이벤트 발행
        log.info("🔥 LoginEvent 발행 - memberEmail: {}", loginRequest.getEmail());
        eventPublisher.publishEvent(
            LoginEvent.builder()
                .memberEmail(loginRequest.getEmail())
                .build()
        );

        return processSignin(authentication);
    }

    /**
     * email 만으로 자동 로그인 처리를 진행하는 메서드<br>
     * !!주의 : 이메일 인증이 사전에 진행되는 로직에서만 사용
     * @param email 사용자 이메일
     * @return {@code TokenDto}
     */
    public TokenDto autoSignin(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return processSignin(authenticationToken);
    }

    /**
     * Spring Security 에서 인증을 수동으로 처리한 후,<br>
     * 인증 객체를 SecurityContext에 직접 설정하고,<br>
     * 인증된 사용자 정보를 기반으로 토큰 발급
     * @param authentication
     * @return {@code TokenDto}
     */
    public TokenDto processSignin(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String roles =  String.join(",", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return processTokenSignin(authentication.getName(), roles);
    }
    
    public TokenDto processTokenSignin(String email, String roles) {
        // black list 에 있다면 해제
        userBlackListRepository.deleteById(email);

        // 로그인시 자동으로 로그인 기록 저장
        loginLogService.newLog(email);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        AccessTokenDto accessToken = jwtTokenProvider.generateAccessToken(email, roles);
        RefreshToken refreshToken = refreshTokenService.saveWithAtId(accessToken.getJti());
        
        return TokenDto.builder()
                   .accessToken(accessToken.getToken())
                   .atId(accessToken.getJti())
                   .refreshToken(refreshToken.getToken())
                   .grantType("Bearer")
                   .refreshExpiresIn(jwtTokenProvider.getRefreshTokenExpiration())
                   .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
                   .build();
    }

    /**
     * Redis에 사용자 이메일을 키 값으로 하여, 인증 번호를 저장<br>
     * TTL 기본 3분
     * @param email 사용자 이메일
     * @param code 인증 코드
     */
    public void saveVerifyCode(String email, String code) {
        String redisKey = "email:verify:" + email;
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(3));
    }

    /**
     * Redis에 이메일, 인증코드로 저장된 데이터를 기반으로, 인증코드 검증
     * @param email 사용자 이메일
     * @param userInputCode 입력받은 인증 코드
     * @return
     */
    public boolean isVerifyCode(String email, String userInputCode) {
        String redisKey = "email:verify:" + email;
        Object raw = redisTemplate.opsForValue().get(redisKey);

        if (raw instanceof String storedCode && storedCode.equals(userInputCode)) {
            redisTemplate.delete(redisKey); // 인증에 성공했으면 삭제 (optional)
            return true;
        } else {
            return false;
        }
    }
}
