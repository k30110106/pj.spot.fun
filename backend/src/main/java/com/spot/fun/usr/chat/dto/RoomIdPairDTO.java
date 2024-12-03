package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomIdPairDTO {
  private Long userRoomId;    // 현재 사용자의 채팅방 ID
  private Long otherRoomId;   // 상대방의 채팅방 ID
}