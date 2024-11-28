package com.spot.fun.usr.chat.dto;

import com.spot.fun.usr.chat.entity.ChatMessage;
import lombok.*;

// 채팅 메시지 create
// 발송자, 수신자, 메시지, 채팅방 정보가 담겨 요청
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatMessageRequestDTO {
    private Long fromId;
    private Long toId;
    private String msg;
    private Long roomId;
    private java.sql.Timestamp timestamp;

    @Builder
    public ChatMessageRequestDTO(Long fromId, Long toId, String msg, Long roomId) {
        this.fromId = fromId;
        this.toId = toId;
        this.msg = msg;
        this.roomId = roomId;
        this.timestamp = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .fromId(fromId)
                .toId(toId)
                .msg(msg)
                .roomId(roomId)
                .build();
    }
}
