package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@ToString
public class ChatMessageOtherDTO implements ChatMessageDTO{
    private String otherNickname;
    private String msg;
    private java.sql.Timestamp timestamp;

    public ChatMessageOtherDTO(String otherNickname, String msg, java.sql.Timestamp timestamp) {
        this.otherNickname = otherNickname;
        this.msg = msg;
        this.timestamp = timestamp;
    }
}
