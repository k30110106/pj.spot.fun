package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDTO {
    private Long otherIdx;

    @Builder
    public ChatRoomRequestDTO(Long otherIdx) {
        this.otherIdx = otherIdx;
    }

}
