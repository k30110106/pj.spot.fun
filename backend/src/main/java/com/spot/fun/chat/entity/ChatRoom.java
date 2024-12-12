package com.spot.fun.chat.entity;

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
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "otherid")
    private User other;

    @Builder
    public ChatRoom(User user, User other) {
        this.user = user;
        this.other = other;
    }
}
