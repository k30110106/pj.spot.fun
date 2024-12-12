package com.spot.fun.chat.entity;

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

    @Column(name = "from_id", nullable = false)
    private Long fromId;

    @Column(name = "to_id", nullable = false)
    private Long toId;

    @Column(name = "msg", updatable = false, nullable = false)
    private String msg;

    @Column(name = "room_id", unique = true, updatable = false, nullable = false)
    private Long roomId;

    @Column(name = "timestamp", updatable = false, nullable = false)
    private Timestamp timestamp;

    @Column(name = "checked", nullable = false)
    @ColumnDefault("0")
    private boolean checked;

    @Builder
    public ChatMessage(Long fromId, Long toId, String msg, Long roomId, boolean checked) {
//        this.chatId = chatId;
        this.fromId = fromId;
        this.toId = toId;
        this.msg = msg;
        this.roomId = roomId;
        this.timestamp = Timestamp.from(Instant.now());
        this.checked = checked;
    }
}
