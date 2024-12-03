package com.spot.fun.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDTO {
    private Long userId;

    @Builder
    public ChatRoomRequestDTO(Long userId) {
        this.userId = userId;
    }

}
