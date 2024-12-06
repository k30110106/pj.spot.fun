package com.spot.fun.usr.signup.service;


import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.dto.UserRole;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public void signup(UserDTO userDTO) {
    log.info("sign up !!!!!!!!!!!!!!!!!!!!!!!!");
    log.info(userDTO.toString());


    // 유효성 검사: 중복된 userId, email, nickname, phone 체크
    if (userDTO.getUserId() == null || !userDTO.getUserId().matches("^[a-zA-Z0-9]{4,12}$")) {
      throw new IllegalArgumentException("userId:아이디를 입력해주세요");
    }
    if (userRepository.existsByUserId(userDTO.getUserId())) {
      throw new IllegalArgumentException("userId:해당 아이디는 중복입니다.");
    }
    
    if (userDTO.getName() == null || !userDTO.getName().matches("^[a-zA-Z가-힣]+$")) {
      throw new IllegalArgumentException("username:이름을 입력해주세요");
    }

    if (userDTO.getEmail() == null || !userDTO.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      throw new IllegalArgumentException("email:유효한 이메일 형식이 아닙니다.");
    }
    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new IllegalArgumentException("email:해당 이메일은 중복입니다.");
    }

    if (userDTO.getNickname() == null || !userDTO.getNickname().matches("^[a-zA-Z0-9가-힣]{1,12}$")) {
      throw new IllegalArgumentException("nickname:닉네임을 입력해주세요");
    }
    if (userRepository.existsByNickname(userDTO.getNickname())) {
      throw new IllegalArgumentException("nickname:해당 닉네임은 중복입니다.");
    }

    if (userDTO.getPhone() == null || !userDTO.getPhone().matches("^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$")) {
      throw new IllegalArgumentException("phone:유효한 핸드폰 번호를 입력해주세요.");
    }
    if (userRepository.existsByPhone(userDTO.getPhone())) {
      throw new IllegalArgumentException("phone:해당 핸드폰 번호는 중복입니다.");
    }

    if (userDTO.getPassword() == null || !userDTO.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")) {
      throw new IllegalArgumentException("password:비밀번호를 입력해주세요");
    }

    if (userDTO.getZonecode() == null || userDTO.getZonecode().trim().isEmpty()) {
      throw new IllegalArgumentException("zonecode:우편번호를 입력해주세요");
    }
    if (userDTO.getAddress() == null || userDTO.getAddress().trim().isEmpty()) {
      throw new IllegalArgumentException("address:주소를 입력해주세요");
    }

    if (userDTO.getBirthDate() == null || userDTO.getBirthDate().trim().isEmpty()) {
      throw new IllegalArgumentException("birthDate:생년월일을 입력해주세요");
    }

    User user = User.builder()
            .userId(userDTO.getUserId())
            .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
            .name(userDTO.getName())
            .birthDate(userDTO.getBirthDate())
            .nickname(userDTO.getNickname())
            .email(userDTO.getEmail())
            .phone(userDTO.getPhone())
            .zonecode(userDTO.getZonecode())
            .address(userDTO.getAddress())
            .detaileAdd(userDTO.getDetaileAdd())
            .provider("LOCAL")
            .userRole(UserRole.ROLE_USER) // 권한 고정
            .build();

    if (user.getUserId() == null || user.getEmail() == null) {
      log.error("회원가입 실패: 필수 값(userId 또는 email)이 누락되었습니다.");
      throw new IllegalStateException("User 객체에 누락된 필드가 있습니다.");
    }

    userRepository.save(user);
  }

  @Override
  public void socialSignup(UserDTO userDTO) {
    log.info("소셜 회원가입 시작: {}", userDTO);

    // 기본값 설정 (예: userRole)
    if (userDTO.getUserRole() == null) {
      userDTO.setUserRole(UserRole.ROLE_USER);
    }


    if (userDTO.getUserId() == null || !userDTO.getUserId().matches("^[a-zA-Z0-9]{4,12}$")) {
      throw new IllegalArgumentException("userId:아이디를 입력해주세요");
    }
    if (userRepository.existsByUserId(userDTO.getUserId())) {
      throw new IllegalArgumentException("userId:해당 아이디는 중복입니다.");
    }

    if (userDTO.getName() == null || !userDTO.getName().matches("^[a-zA-Z가-힣]+$")) {
      throw new IllegalArgumentException("username:이름을 입력해주세요");
    }

    if (userDTO.getEmail() == null || !userDTO.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      throw new IllegalArgumentException("email:유효한 이메일 형식이 아닙니다.");
    }
    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new IllegalArgumentException("email:해당 이메일은 중복입니다.");
    }

    if (userDTO.getNickname() == null || !userDTO.getNickname().matches("^[a-zA-Z0-9가-힣]{1,12}$")) {
      throw new IllegalArgumentException("nickname:닉네임을 입력해주세요");
    }
    if (userRepository.existsByNickname(userDTO.getNickname())) {
      throw new IllegalArgumentException("nickname:해당 닉네임은 중복입니다.");
    }

    if (userDTO.getPhone() == null || !userDTO.getPhone().matches("^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$")) {
      throw new IllegalArgumentException("phone:유효한 핸드폰 번호를 입력해주세요.");
    }
    if (userRepository.existsByPhone(userDTO.getPhone())) {
      throw new IllegalArgumentException("phone:해당 핸드폰 번호는 중복입니다.");
    }

    if (userDTO.getZonecode() == null || userDTO.getZonecode().trim().isEmpty()) {
      throw new IllegalArgumentException("zonecode:우편번호를 입력해주세요");
    }
    if (userDTO.getAddress() == null || userDTO.getAddress().trim().isEmpty()) {
      throw new IllegalArgumentException("address:주소를 입력해주세요");
    }

    if (userDTO.getBirthDate() == null || userDTO.getBirthDate().trim().isEmpty()) {
      throw new IllegalArgumentException("birthDate:생년월일을 입력해주세요");
    }

    // 비밀번호는 소셜 로그인 시 입력받지 않음
    User user = User.builder()
            .userId(userDTO.getUserId())
            .name(userDTO.getName())
            .birthDate(userDTO.getBirthDate())
            .nickname(userDTO.getNickname())
            .email(userDTO.getEmail())
            .phone(userDTO.getPhone())
            .zonecode(userDTO.getZonecode())
            .address(userDTO.getAddress())
            .detaileAdd(userDTO.getDetaileAdd())
            .provider(userDTO.getProvider())
            .userRole(UserRole.ROLE_USER) // 권한 고정
            .build();

    userRepository.save(user);
    log.info("소셜 회원가입 완료: {}", user);
  }

}
