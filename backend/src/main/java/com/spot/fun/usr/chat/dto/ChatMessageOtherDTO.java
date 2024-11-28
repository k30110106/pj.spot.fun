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
    private String otherName;
    private String msg;
    private java.sql.Timestamp timestamp;

    public ChatMessageOtherDTO(String otherName, String msg, java.sql.Timestamp timestamp) {
        this.otherName = otherName;
        this.msg = msg;
        this.timestamp = timestamp;
    }
}
