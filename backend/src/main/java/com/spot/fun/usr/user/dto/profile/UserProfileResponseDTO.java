package com.spot.fun.usr.user.dto.profile;

import com.spot.fun.usr.user.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {
  private Long userIdx;
  private String uploadName;
  private String originName;
  private String description;

  private UserDTO user;
}
