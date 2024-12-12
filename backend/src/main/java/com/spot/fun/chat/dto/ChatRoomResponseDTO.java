package com.spot.fun.chat.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 내 채팅방 목록 조회(1님과의 대화, 2님과의 대화 ... 내가 참여한 대화 방이 리스트로...)
@Builder
@Getter
@Data
@NoArgsConstructor
public class ChatRoomResponseDTO {
    private Long roomId;
    private String otherUserName;
    private String recentMessage;
    private java.sql.Timestamp recentMessageTimestamp;
    private boolean recentMessageRead;

    public ChatRoomResponseDTO(Long roomId, String otherUserName, String recentMessage, java.sql.Timestamp recentMessageTimestamp, boolean recentMessageRead) {
        this.roomId = roomId;
        this.otherUserName = otherUserName;
        this.recentMessage = recentMessage;
        this.recentMessageTimestamp = recentMessageTimestamp;
        this.recentMessageRead = recentMessageRead;
    }


}
