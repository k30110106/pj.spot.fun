package com.spot.fun.usr.user.service.profile;

import com.spot.fun.usr.user.dto.profile.UserProfileRequestDTO;
import com.spot.fun.usr.user.dto.profile.UserProfileResponseDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.entity.profile.UserProfile;
import com.spot.fun.usr.user.repository.UserRepository;
import com.spot.fun.usr.user.repository.profile.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final UserRepository userRepository;

  @Override
  public UserProfileResponseDTO getProfile(UserProfileRequestDTO userProfileRequestDTO) {
    Long userIdx = userProfileRequestDTO.getUserIdx();

    UserProfile profile = userProfileRepository.findByUserIdx(userIdx)
            .orElseGet(() -> {
              try {
                Constructor<UserProfile> constructor = UserProfile.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
              } catch (Exception e) {
                throw new RuntimeException("UserProfile 객체 생성 실패", e);
              }
            });

    if (Objects.isNull(profile.getUser())) {
      User user = userRepository.findByIdx(userIdx)
              .orElseThrow(IllegalArgumentException::new);
      profile.changeUser(user);
    }

    return profile.toDTO();
  }
}
