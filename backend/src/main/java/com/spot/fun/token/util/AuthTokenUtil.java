package com.spot.fun.token.util;

import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.token.entity.AuthToken;
import com.spot.fun.token.repository.AuthTokenRepository;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthTokenUtil {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.access.token.min}")
    private int ACCESS_TOKEN_MIN; // 30분
    @Value("${jwt.refresh.token.hour}")
    private int REFRESH_TOKEN_HOUR; // 1시간

    public void makeCookie(HttpServletResponse response, String cookieName, String cookieValue, Duration duration) {
        ResponseCookie responseCookie =
                ResponseCookie.from(cookieName, cookieValue)
                        .httpOnly(true) // 자바스크립트 접근불가
//                        .sameSite("None") // SameSite 설정
                        //.secure(true) // https에서만 전송처리
                        .path("/")
                        .maxAge(duration) // 유효 기간
                        .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void makeAccessToken(HttpServletResponse response, String cookieValue) {
        makeCookie(response, "access_token", cookieValue, Duration.ofMinutes(ACCESS_TOKEN_MIN));
    }

    public void makeRefreshToken(HttpServletResponse response, String cookieValue) {
        makeCookie(response, "refresh_token", cookieValue, Duration.ofHours(REFRESH_TOKEN_HOUR));
    }

    public boolean deleteAccessToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            makeCookie(response, "access_token", null, Duration.ZERO);
            return true;
        } catch (Exception e) {
            log.error("deleteAccessToken fail .. {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            makeCookie(response, "refresh_token", null, Duration.ZERO);
            return true;
        } catch (Exception e) {
            log.error("deleteRefreshToken fail .. {}", e.getMessage());
            return false;
        }
    }

    public String getTokenValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String resultStr = null;
        if (!Objects.isNull(cookies)) {
            resultStr = Arrays.stream(cookies)
                    .filter((cookie) -> cookie.getName().equalsIgnoreCase(cookieName))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return resultStr;
    }

    private String getAccessToken(HttpServletRequest request) {
        return getTokenValue(request, "access_token");
    }

    private String getRefreshToken(HttpServletRequest request) {
        return getTokenValue(request, "refresh_token");
    }

    private User getUser(HttpServletRequest request) {
        try {
            String refreshToken = getRefreshToken(request);
            AuthToken authToken = authTokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("데이터베이스에서 조회할 수 없는 토큰"));
            User user = userRepository.findByIdx(authToken.getUserIdx())
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 사용자"));
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean validateAccessToken(HttpServletRequest request) {
        String tokenValue = getAccessToken(request);
        return !StringUtils.isBlank(tokenValue) && jwtTokenProvider.validToken(tokenValue);
    }

    public boolean validateRefreshToken(HttpServletRequest request) {
        String tokenValue = getRefreshToken(request);
        if (!StringUtils.isBlank(tokenValue)) {
            if (jwtTokenProvider.validToken(tokenValue) && authTokenRepository.existsByRefreshToken(tokenValue)) {
                return true;
            }
            authTokenRepository.deleteByRefreshToken(tokenValue);
        }
        return false;
    }




    /**
     * 엑세스 토큰 검증 + 리프레시 토큰 검증 + 엑세스 토큰 재발급
     * >> 성공 :: userDTO 반환 (fromEntity)
     * >> 실패 :: 빈 객체 반환 (리프레시 토큰이 없거나 사용불가)
     *
     * @param request
     * @param response
     * @return
     */
    public UserDTO validateTokenAndGetUserDTO(HttpServletRequest request, HttpServletResponse response) {
        if (validateAccessToken(request)) { // 엑세스 토큰 유효검사
            User user = getUser(request);
            return UserDTO.fromEntity(user);
        }

        if (validateRefreshToken(request)) { // 리프레시 토큰 유효검사
            try {
                User user = getUser(request);
                String newAccessToken = jwtTokenProvider.generateAccessToken(user);
                makeAccessToken(response, newAccessToken); // 엑세스 토큰 재발급 + 쿠키 추가
                return UserDTO.fromEntity(user);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new UserDTO();
            }
        }

        return new UserDTO();
    }

    /***
     * 엑세스 토큰 삭제 + 리프레시 토큰 삭제 + 데이터베이스에 저장 된 리프레시토큰 정보 삭제
     * >> 성공 : true 반환
     * >> 실패 : false 반환
     *
     * @param request
     * @param response
     * @return
     */
    public boolean removeTokenAndLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String accessToken = getAccessToken(request);
            if(!StringUtils.isBlank(accessToken)) { // 엑세스 토큰 삭제
                deleteAccessToken(request, response);
            }

            String refreshToken = getRefreshToken(request);
            if(!StringUtils.isBlank(refreshToken)) { // 리프레시 토큰 삭제
                authTokenRepository.deleteByRefreshToken(refreshToken);
                deleteRefreshToken(request, response);
            }
            return true;
        } catch (Exception e) {
            log.error("removeTokenAndLogout fail .. {}", e.getMessage());
            return false;
        }
    }
}
