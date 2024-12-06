package com.spot.fun.usr.user.service;

import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(entity -> {
                    // 비밀번호가 없는 경우(소셜 로그인 사용자)는 빈 문자열 처리
                    String password = entity.getPassword() == null ? "" : entity.getPassword();

                    return User.builder()
                            .name(entity.getUserId()) // 로그인 ID
                            .password(password) // 비밀번호 (소셜 로그인 시 빈 문자열)
                            .userRole(entity.getUserRole()) // 역할(Role)
                            .build();
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));
    }

//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        return userRepository.findByUserId(userId)
////                .orElseThrow(IllegalArgumentException::new);
//                    .orElseThrow(() -> new IllegalArgumentException("not found Exception.. !!! " + userId + " !!!"));
//    }


    public String getUserRole(Long idx) {
        return userRepository.findByIdx(idx)
                .orElseThrow(IllegalArgumentException::new)
                .toDTO().getUserRole().getRole();
    }

}

