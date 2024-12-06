package com.spot.fun.usr.login.service;

import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.token.dto.AuthTokenDTO;
import com.spot.fun.token.entity.AuthToken;
import com.spot.fun.token.repository.AuthTokenRepository;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;

    @Override
    public AuthTokenDTO doLogin(UserDTO userDTO) {
        // 403 << 로그인 실패 시 응답코드
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUserId(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        Long userIdx = user.getIdx();

//        // 자체 로그인 시 email 필드가 null일 수 있으므로 기본값 설정
//        if (user.getProvider() == null || "LOCAL".equalsIgnoreCase(user.getProvider())) {
//            log.info("LOCAL login detected for userId={}", user.getUserId());
//            if (user.getEmail() == null) {
//                user = User.builder()
//                        .idx(user.getIdx())
//                        .userId(user.getUserId())
//                        .nickname(user.getNickname())
//                        .email("default@local.com") // 기본 email 값 설정
//                        .provider("LOCAL") // LOCAL 로그인
//                        .userRole(user.getUserRole())
//                        .build();
//            }
//        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        if(authTokenRepository.existsByUserIdx(userIdx)) {
            authTokenRepository.deleteByUserIdx(userIdx);
        }

        AuthToken authToken = authTokenRepository.save(
                AuthToken.builder()
                        .refreshToken(refreshToken)
                        .userIdx(userIdx)
                        .build()
        );

        return AuthTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .build();
    }

}
