package com.spot.fun.usr.chat.repository;

import com.spot.fun.usr.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    Optional<ChatMessage> findByRoomId(Long roomId);
    Optional<ChatMessage> findByChatId(Long chatId);
    Optional<ChatMessage> findByRoomIdAndFromId(Long roomId, Long fromId);
    Optional<ChatMessage> findByRoomIdAndToId(Long roomId, Long toId);
    ChatMessage findTopByRoomIdOrderByTimestampDesc(Long roomId);
    List<Long> findChatIdByRoomId(Long roomId);
    Long findFromIdByChatId(Long chatId);
    Long findToIdByChatId(Long chatId);
//    List<Long> findAllChatIdByRoomId(Long roomId);
    ChatMessage findChatMessageByChatId(Long chatId);
}
