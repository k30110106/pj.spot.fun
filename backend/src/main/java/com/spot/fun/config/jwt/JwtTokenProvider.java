package com.spot.fun.config.jwt;

import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.access.token.min}")
    private int ACCESS_TOKEN_MIN;

    @Value("${jwt.refresh.token.hour}")
    private int REFRESH_TOKEN_HOUR;

    public String generateAccessToken(User user) {
        return generateToken(user, Duration.ofMinutes(ACCESS_TOKEN_MIN));
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, Duration.ofHours(REFRESH_TOKEN_HOUR));
    }

    private String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(user, new Date(now.getTime() + expiredAt.toMillis()));
    }

    private String makeToken(User user, Date expiry) {
        Date now = new Date();
        return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuer(jwtProperties.getIssuer())
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .setSubject(user.getUserId())
                    .claim("idx", user.getIdx())
                    .claim("nickname", user.getNickname())
                    .claim("email", user.getEmail())
                    .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                    .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("\n======== validToken method fail ========");
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        //Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        Long idx = claims.get("idx", Long.class);
        String userRole = customUserDetailsService.getUserRole(idx);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(userRole));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),
                token,
                authorities
        );
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdx(String token) {
        Claims claims = getClaims(token);
        return claims.get("idx", Long.class);
    }

    public String getUserNickname(String token) {
        Claims claims = getClaims(token);
        return claims.get("nickname", String.class);
    }
}
