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
    private Long fromIdx;
    private Long toIdx;
    private String msg;
    private Long roomId;
    private java.sql.Timestamp timestamp;

    @Builder
    public ChatMessageRequestDTO(Long fromIdx, Long toIdx, String msg, Long roomId) {
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
        this.msg = msg;
        this.roomId = roomId;
        this.timestamp = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .fromIdx(fromIdx)
                .toIdx(toIdx)
                .msg(msg)
                .roomId(roomId)
                .build();
    }
}
