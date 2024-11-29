package com.spot.fun.usr.chat.repository;

import com.spot.fun.usr.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    ChatMessage findTopByRoomIdOrderByTimestampDesc(Long roomId);
    List<Long> findChatIdByRoomId(Long roomId);
    ChatMessage findChatMessageByChatId(Long chatId);
}
