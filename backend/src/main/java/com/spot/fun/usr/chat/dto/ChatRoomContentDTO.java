package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Data
@NoArgsConstructor  // otherId 와의 채팅방 입장 시 브라우저에 표현될 채팅방 content를 담음
public class ChatRoomContentDTO {
  private String otherNickname;
  private String otherProfileImg;
  private String otherPeedUrl;

  public ChatRoomContentDTO(String otherNickname, String otherProfileImg, String otherPeedUrl) {
    this.otherNickname = otherNickname;
    this.otherProfileImg = otherProfileImg;
    this.otherPeedUrl = otherPeedUrl;
  }
}
