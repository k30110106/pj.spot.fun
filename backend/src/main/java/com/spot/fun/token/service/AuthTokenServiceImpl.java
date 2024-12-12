package com.spot.fun.token.service;

import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.token.dto.AuthTokenDTO;
import com.spot.fun.token.entity.AuthToken;
import com.spot.fun.token.repository.AuthTokenRepository;
import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final AuthTokenUtil authTokenUtil;

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

    /**
     * OAuth 로그인 시 Refresh Token 생성 및 저장
     */
    public void handleOAuthLogin(User user, String refreshToken) {
        log.info("Handling OAuth login for user: {}", user.getEmail());

        AuthToken authToken = authTokenRepository.findByUserIdx(user.getIdx())
                .orElse(AuthToken.builder()
                        .userIdx(user.getIdx())
                        .refreshToken(refreshToken)
                        .build());
        authToken.update(refreshToken); // Refresh Token 갱신
        authTokenRepository.save(authToken);
    }

    // 자체로그인 컨트롤러에서 호출시 에러남
//    /**
//     * 자체 로그인 시 Refresh Token 생성 및 저장
//     */
//    public void handleLocalLogin(User user, String refreshToken) {
//        log.info("Handling Local login for user: {}", user.getEmail());
//
//        AuthToken authToken = authTokenRepository.findByUserIdx(user.getIdx())
//                .orElse(AuthToken.builder()
//                        .userIdx(user.getIdx())
//                        .refreshToken(refreshToken)
//                        .build());
//        authToken.update(refreshToken); // Refresh Token 갱신
//        authTokenRepository.save(authToken);
//    }

    /**
     * Refresh Token을 쿠키에서 추출
     */
    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Long getCurrentUserIdx() {
        System.out.println("getCurrentUserIdx" + Arrays.toString(request.getCookies()));
        log.info("getCurrentUserIdx : " + authTokenUtil.validateTokenAndGetUserDTO(request, response).getIdx());
        return authTokenUtil.validateTokenAndGetUserDTO(request, response).getIdx();
    }
}
