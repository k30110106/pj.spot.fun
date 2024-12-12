package com.spot.fun.chat.repository;

import com.spot.fun.chat.entity.ChatRoom;
import com.spot.fun.usr.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findChatRoomByRoomId(Long roomId);
    Optional<ChatRoom> findChatRoomByUserAndOther(User user, User other);
//    Optional<ChatRoom> findChatRoomByUserId(Long userId);
    Optional<ChatRoom> findChatRoomByUser(User user);
    Long findUserIdByRoomId(Long roomId);
    Long findOtherIdByRoomId(Long roomId);
}
