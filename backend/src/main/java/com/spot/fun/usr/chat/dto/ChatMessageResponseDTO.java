package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@ToString
public class ChatMessageResponseDTO{
  private Long fromIdx;
  private String msg;
  private java.sql.Timestamp timestamp;

  public ChatMessageResponseDTO(Long fromIdx, String msg, java.sql.Timestamp timestamp) {
    this.fromIdx = fromIdx;
    this.msg = msg;
    this.timestamp = timestamp;
  }
}
