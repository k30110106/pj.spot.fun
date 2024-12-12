package com.spot.fun.usr.user.entity.profile;

import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.dto.profile.UserProfileResponseDTO;
import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "tbl_user_profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserProfile {
  @Id
  @Column(name = "user_idx")
  private Long userIdx;

  @Column(name = "upload_name")
  private String uploadName;

  @Column(name = "origin_name")
  private String originName;

  @Column(name = "description")
  private String description;

  @OneToOne
  @MapsId  // userIdx를 기본 키로 사용하도록 설정
  @JoinColumn(name = "user_idx")
  private User user;

  public void changeUser(User user) {
    this.user = user;
  }

  public void changeUserIdx(Long userIdx) {
    this.userIdx = userIdx;
  }

  public UserProfileResponseDTO toDTO() {
    return UserProfileResponseDTO.builder()
            .userIdx(this.userIdx)
            .originName(this.originName)
            .uploadName(this.uploadName)
            .description(this.description)
            .user(UserDTO.builder()
                    .idx(this.user.getIdx())
                    .userId(this.user.getUserId())
                    .nickname(this.user.getNickname())
                    .build())
            .build();
  }
}
