package com.spot.fun.usr.chat.repository;

import com.spot.fun.usr.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    ChatMessage findTopByRoomIdOrderByTimestampDesc(Long roomId);

    @Query("SELECT m.chatId FROM ChatMessage m WHERE m.roomId = :roomId")
    List<Long> findChatIdByRoomId(@Param("roomId") Long roomId);
    ChatMessage findChatMessageByChatId(Long chatId);

    // 양방향 roomId로 메시지 조회
    @Query("SELECT m FROM ChatMessage m WHERE m.roomId IN (:userRoomId, :otherRoomId) ORDER BY m.timestamp ASC")
    List<ChatMessage> findAllByRoomIds(@Param("userRoomId") Long userRoomId, @Param("otherRoomId") Long otherRoomId);
}
