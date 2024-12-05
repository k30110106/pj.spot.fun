package com.spot.fun.usr.user.repository;

import com.spot.fun.usr.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
    Optional<User> findByIdx(Long idx);
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndBirthDateAndEmail(String name, String birthDate, String email);
    Optional<User> findByUserIdAndEmail(String userId, String email);
}
