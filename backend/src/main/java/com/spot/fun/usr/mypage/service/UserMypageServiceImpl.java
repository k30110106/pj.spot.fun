package com.spot.fun.usr.mypage.service;

import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.config.jwt.JwtTokenUtil;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMypageServiceImpl implements UserMypageService {
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public UserDTO findByIdx() {
    String accessToken = JwtTokenUtil.getJwtToken();
    Long userIdx = jwtTokenProvider.getUserIdx(accessToken);
    User user = userRepository.findByIdx(userIdx)
            .orElseThrow(IllegalArgumentException::new);

    return UserDTO.builder()
            .userId(user.getUserId())
            .password(user.getPassword())
            .userRole(user.getUserRole())
            .build();
  }

  @Override
  public UserDTO getUser(UserDTO userDTO) {
    Long userIdx = userDTO.getIdx();
    User user = userRepository.findByIdx(userIdx)
            .orElseThrow(IllegalArgumentException::new);

    return UserDTO.builder()
            //.userId(user.getUserId())
            .nickname(user.getNickname())
            .build();
  }
}
