package com.spot.fun.usr.chat.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name="tbl_chatmsg")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", unique = true, updatable = false, nullable = false)
    private Long chatId;

    @Column(name = "from_idx", nullable = false)
    private Long fromIdx;

    @Column(name = "to_idx", nullable = false)
    private Long toIdx;

    @Column(name = "msg", updatable = false, nullable = false)
    private String msg;

    @Column(name = "room_id", unique = true, updatable = false, nullable = false)
    private Long roomId;

    @Column(name = "timestamp", updatable = false, nullable = false)
    private Timestamp timestamp;

    @Column(name = "is_read", nullable = false)
    @ColumnDefault("0")
    private boolean isRead;

    @Builder
    public ChatMessage(Long fromIdx, Long toIdx, String msg, Long roomId, boolean isRead) {
//        this.chatId = chatId;
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
        this.msg = msg;
        this.roomId = roomId;
        this.timestamp = Timestamp.from(Instant.now());
        this.isRead = isRead;
    }

    // 채팅 생성
    public static ChatMessage createChatMessage(Long fromIdx, Long toIdx, String msg, Long roomId, boolean isRead) {
        return ChatMessage.builder()
                .fromIdx(fromIdx)
                .toIdx(toIdx)
                .msg(msg)
                .roomId(roomId)
                .isRead(isRead)
                .build();
    }
}
