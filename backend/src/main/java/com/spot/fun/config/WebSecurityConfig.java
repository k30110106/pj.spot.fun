package com.spot.fun.config;

import com.spot.fun.config.jwt.JwtTokenFilter;
import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.oauthlogin.service.CustomOAuth2UserService;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenUtil authTokenUtil;

    @Value("${security.check.path.none}")
    private String[] PERMITTED_PATHS;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/usr/mypage/**").hasAuthority("USER")
                        .requestMatchers(PERMITTED_PATHS).permitAll()
                        .anyRequest().authenticated()
                )
                .cors((auth) -> auth
                        .configurationSource(corsConfigurationSource())
                )
                .csrf((auth) -> auth
                        .disable()
                )
                .sessionManagement((auth) -> auth // 세션방식 -> jwt 사용
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService) // 최신 방식 적용
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler()) // OAuth2 성공 핸들러 추가
                        .failureHandler(oAuth2AuthenticationFailureHandler())
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000"); // 허용 origin 지정
        configuration.addAllowedOrigin("http://localhost:3001"); // 추가 origin

        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE")); // 클러라이언트 요청허용 범위설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // HTTP헤더 설정(인증,인가,컨텐츠타입 등 클라이언트에서 서버로 요청할 때 사용가능 범위설정)
        configuration.setAllowCredentials(true); // 쿠키,인증정보 등 요청허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든경로에 대하여 위 설정을 적용

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("http://localhost:3000/login?error=true");
        };
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
                Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes()); // 수정 가능 Map으로 변환
                String email = (String) attributes.get("email");

                if (email == null) {
                    log.error("OAuth2 Success Handler - Email is null, redirecting to error page.");
                    response.sendRedirect("http://localhost:3000/login-error");
                    return;
                }

                log.info("OAuth2 Success Handler - Email: {}", email);

                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                    // 기존 사용자: JWT 생성 후 로그인 성공 페이지로 리다이렉트
                    User user = userOptional.get();
                    String accessToken = jwtTokenProvider.generateAccessToken(user);
                    String refreshToken = jwtTokenProvider.generateRefreshToken(user);

                    authTokenUtil.makeAccessToken(response, accessToken);
                    authTokenUtil.makeRefreshToken(response, refreshToken);

                    log.info("프론트엔드의 login-success 페이지로 리다이렉트...");
                    response.sendRedirect("http://localhost:3000/login-success");
                } else {
                    // 비회원: 회원가입 페이지로 리다이렉트
                    log.info("Redirecting to social-signup for new user: {}", email);
                    response.sendRedirect("http://localhost:3000/social-signup");
                }
            }
        };
    }
}
