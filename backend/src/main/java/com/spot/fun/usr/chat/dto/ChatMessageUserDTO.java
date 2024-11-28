package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@ToString
public class ChatMessageUserDTO implements ChatMessageDTO {
    private String msg;
    private java.sql.Timestamp timestamp;

    public ChatMessageUserDTO(String msg, java.sql.Timestamp timestamp) {
        this.msg = msg;
        this.timestamp = timestamp;
    }
}
