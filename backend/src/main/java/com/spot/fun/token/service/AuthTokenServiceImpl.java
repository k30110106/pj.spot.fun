package com.spot.fun.token.service;

import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.token.dto.AuthTokenDTO;
import com.spot.fun.token.entity.AuthToken;
import com.spot.fun.token.repository.AuthTokenRepository;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    @Override
    public AuthTokenDTO doRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();  // 리프레시 토큰을 추출
                    break;
                }
            }
        }

        if(StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validToken(refreshToken)) {
            // 쿠키에서 리프레시 토큰조회 + 사용가능여부 체크
            //throw new RuntimeException("refresh token is empty or validate failed");
            // 에러콘솔 거슬려서.. 일단 빈객체로 리턴처리
            return AuthTokenDTO.builder()
                    .accessToken(null)
                    .refreshToken(null)
                    .nickname(null)
                    .build();
        }

        // 엑세스 토큰 재발급
        AuthToken authToken = authTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("데이터베이스에서 조회할 수 없는 토큰"));
        User user = userRepository.findByIdx(authToken.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 사용자"));
        String accessToken = jwtTokenProvider.generateAccessToken(user);

        return AuthTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(null) // 리프레시 토큰은 클라이언트에 반환하지 않음
                .nickname(user.getNickname())
                .build();
    }

    public Long getCurrentUserIdx() {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtTokenProvider.getUserIdx(token);
    }
}
