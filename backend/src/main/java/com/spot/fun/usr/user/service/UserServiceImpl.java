package com.spot.fun.usr.user.service;

import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    @Override
    public UserDTO findByIdx(Long idx) {
        return userRepository.findByIdx(idx)
                .orElseThrow(IllegalArgumentException::new)
                .toDTO();
    }

    @Override
    public UserDTO findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new)
                .toDTO();
    }

    @Override
    public String findUserIdByDetails(String name, String birthDate, String email) {
        // UserRepository를 통해 사용자 정보 조회
        return userRepository.findByNameAndBirthDateAndEmail(name, birthDate, email)
                .map(User::getUserId) // 찾은 사용자의 아이디를 반환
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }

    @Override
    public void updatePassword(String userId, String email, String newPassword) {
        // 사용자 정보 확인
        log.info("비밀번호 변경 요청: userId={}, email={}", userId, email);

        User user = userRepository.findByUserIdAndEmail(userId, email)
                .orElseThrow(() -> {
                    log.error("사용자 정보를 찾을 수 없음: userId={}, email={}", userId, email);
                    return new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
                });

        // 비밀번호 암호화 및 설정
        String encodedPassword = bcryptPasswordEncoder.encode(newPassword);
        log.info("암호화된 비밀번호 생성 완료");

        try {
            user.updatePassword(encodedPassword);
            userRepository.save(user);
            log.info("비밀번호 변경 완료: userId={}", userId);
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생", e);
            throw new RuntimeException("비밀번호 변경 중 문제가 발생했습니다.", e);
        }
    }

    public Optional<User> findUserByIdx(Long idx){
        return userRepository.findByIdx(idx);
    }

}
