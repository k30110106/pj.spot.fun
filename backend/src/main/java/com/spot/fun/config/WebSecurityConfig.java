package com.spot.fun.config;

import com.spot.fun.config.jwt.JwtTokenFilter;
import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.token.util.AuthTokenUtil;
//import com.spot.fun.usr.oauthlogin.service.CustomOAuth2UserService;
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
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
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
//    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenUtil authTokenUtil;

    @Value("${security.check.path.none}")
    private String[] PERMITTED_PATHS;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션방식 -> JWT 사용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/usr/mypage/**").hasAuthority("USER")
                        .requestMatchers("/api/usr/datecourse/public", "api/usr/datecourse/user").permitAll()
                        // WebSocket 엔드포인트 허용
                        .requestMatchers("/ws/**").permitAll()
//                        .requestMatchers("/api/chat/**").permitAll()
                        .requestMatchers("/api/chat/**").authenticated()  // 명시적으로 인증 필요함을 표시
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
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))  // OAuth2 로그인 설정
//                        .successHandler(oAuth2AuthenticationSuccessHandler())  // 로그인 성공 시 동작
//                        .failureHandler(oAuth2AuthenticationFailureHandler()))  // 로그인 실패 시 동작
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용하는 Origin 설정
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:3001");

        // 요청 방식 설정
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));

        // 허용할 HTTP 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // 인증 정보 허용
        configuration.setAllowCredentials(true);

        // 모든 경로에 대해 CORS 정책 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든경로에 대하여 위 설정을 적용

        return source;
    }

    // BCryptPasswordEncoder Bean 설정
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    // OAuth2 로그인 실패 핸들러
//    @Bean
//    public AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
//        return (request, response, exception) -> {
//            response.sendRedirect("http://localhost:3000/login?error=true");
//        };
//    }
//
//    // OAuth2 로그인 성공 핸들러
//    @Bean
//    public SimpleUrlAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
//        return new SimpleUrlAuthenticationSuccessHandler() {
//            @Override
//            protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
//                Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes()); // 수정 가능 Map으로 변환
//                String email = (String) attributes.get("email");
//
//                if (email == null) {
//                    log.error("OAuth2 Success Handler - Email is null, redirecting to error page.");
//                    response.sendRedirect("http://localhost:3000/login-error");
//                    return;
//                }
//
//                log.info("OAuth2 Success Handler - Email: {}", email);
//
//                Optional<User> userOptional = userRepository.findByEmail(email);
//                if (userOptional.isPresent()) {
//                    // 기존 사용자: JWT 생성 후 로그인 성공 페이지로 리다이렉트
//                    User user = userOptional.get();
//                    String accessToken = jwtTokenProvider.generateAccessToken(user);
//                    String refreshToken = jwtTokenProvider.generateRefreshToken(user);
//
//                    authTokenUtil.makeAccessToken(response, accessToken);
//                    authTokenUtil.makeRefreshToken(response, refreshToken);
//
//                    log.info("프론트엔드의 login-success 페이지로 리다이렉트...");
//                    response.sendRedirect("http://localhost:3000/login-success");
//                } else {
//                    // 비회원: 회원가입 페이지로 리다이렉트
//                    log.info("Redirecting to social-signup for new user: {}", email);
//                    response.sendRedirect("http://localhost:3000/social-signup");
//                }
//            }
//        };
//    }
}
