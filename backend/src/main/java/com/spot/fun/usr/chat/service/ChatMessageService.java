package com.spot.fun.usr.chat.service;

import com.spot.fun.usr.chat.dto.*;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// 채팅방 메시지 저장하는 로직
@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatService{
    private final ChatMessageRepository chatMessageRepository;

    // 채팅 전송
//    public void save(ChatMessageRequestDTO chatMessageRequestDTO) {
//        ChatMessage chatMessage = chatMessageRequestDTO.toEntity();
//        chatMessageRepository.save(chatMessage);
//    }

    public ChatRoomListResponseDTO setChatRoomListResponseDTO(Long roomId) {
        ChatMessage recendChatMessage = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(roomId);
        return ChatRoomListResponseDTO.builder()
                .recentMessage(recendChatMessage.getMsg())
                .recentMessageTimestamp(recendChatMessage.getTimestamp())
                .isRecentMessageRead(recendChatMessage.isRead())
                .build();
    }

    public List<Long> findChatIdListByRoomId(Long roomId) {
        return chatMessageRepository.findChatIdByRoomId(roomId);
    }

    public ChatMessage findChatMessageById(Long chatId) {
        return chatMessageRepository.findChatMessageByChatId(chatId);
    }

    public ChatMessageDTO setChatMessageDTO(Long userIdx, ChatMessage chatMessage) {
        if(Objects.equals(userIdx,chatMessage.getFromId())) {
            return ChatMessageUserDTO.builder()
                    .msg(chatMessage.getMsg())
                    .timestamp(chatMessage.getTimestamp())
                    .build();
        }
        if(Objects.equals(userIdx,chatMessage.getToId())) {
            return ChatMessageOtherDTO.builder()
                    .msg(chatMessage.getMsg())
                    .timestamp(chatMessage.getTimestamp())
                    .build();
        }
        return null;
    }

//    public ChatMessage createChatMessageForUser(ChatMessage chatMessage) {
//        return ChatMessage.builder()
//                .
//    }

}
