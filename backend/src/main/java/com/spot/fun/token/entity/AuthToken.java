package com.spot.fun.token.entity;

import com.spot.fun.token.repository.AuthTokenRepository;
import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tbl_auth_token")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@RequiredArgsConstructor
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", unique = true, updatable = false)
    private Long idx;

    @Column(name = "user_idx", nullable = false, unique = true)
    private Long userIdx;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Builder
    public AuthToken(Long userIdx, String refreshToken) {
        this.userIdx = userIdx;
        this.refreshToken = refreshToken;
    }

    public AuthToken update(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
