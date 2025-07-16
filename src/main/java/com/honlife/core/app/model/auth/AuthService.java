package com.honlife.core.app.model.auth;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.honlife.core.app.controller.auth.payload.LoginRequest;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.auth.token.RefreshTokenService;
import com.honlife.core.app.model.auth.token.UserBlackListRepository;
import com.honlife.core.app.model.auth.token.entity.RefreshToken;
import com.honlife.core.infra.auth.jwt.JwtTokenProvider;
import com.honlife.core.infra.auth.jwt.dto.AccessTokenDto;

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

    public TokenDto signin(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword());
        
        // loadUserByUsername + password 검증 후 인증 객체 반환
        // 인증 실패 시: AuthenticationException 발생
        Authentication authentication = authenticationManagerBuilder.getObject()
                                            .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String roles =  String.join(",", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return processTokenSignin(authentication.getName(), roles);
    }
    
    public TokenDto processTokenSignin(String email, String roles) {
        // black list 에 있다면 해제
        userBlackListRepository.deleteById(email);
        
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
