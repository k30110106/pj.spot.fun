package com.spot.fun.usr.user.dto.profile;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequestDTO {
  private Long userIdx;
  private String uploadName;
}
