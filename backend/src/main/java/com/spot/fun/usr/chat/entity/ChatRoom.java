package com.spot.fun.usr.chat.entity;

import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="tbl_chatroom")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomid", unique = true, updatable = false)
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "useridx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "otheridx")
    private User other;

    @Builder
    public ChatRoom(Long roomId, User user, User other) {
        this.roomId = roomId;
        this.user = user;
        this.other = other;
    }
}
