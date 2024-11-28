package com.spot.fun.usr.chat.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 내 채팅방 목록 조회(1님과의 대화, 2님과의 대화 ... 내가 참여한 대화 방이 리스트로...)
@Builder(toBuilder = true)
@Getter
@Data
@NoArgsConstructor
public class ChatRoomResponseDTO {
//    private Long roomId;
    private String otherNickname;
    private String recentMessage;
    private java.sql.Timestamp recentMessageTimestamp;
    private boolean isRecentMessageRead;
    private String otherProfileImg;
    private String otherPeedUrl;

    public ChatRoomResponseDTO(String otherNickname, String recentMessage, java.sql.Timestamp recentMessageTimestamp, boolean isRecentMessageRead, String otherProfileImg, String otherFeedUrl) {
//        this.roomId = roomId;
        this.otherNickname = otherNickname;
        this.recentMessage = recentMessage;
        this.recentMessageTimestamp = recentMessageTimestamp;
        this.isRecentMessageRead = isRecentMessageRead;
        this.otherProfileImg = otherProfileImg;
        this.otherPeedUrl = otherFeedUrl;
    }
}
