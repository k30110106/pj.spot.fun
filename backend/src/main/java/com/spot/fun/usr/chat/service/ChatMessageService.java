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

    // 채팅 메시지 저장 - 하나의 메시지만 저장
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

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

    // 양쪽 채팅방에 메시지 저장
    public ChatMessage saveMessageForBothRooms(Long userRoomId, Long otherRoomId, ChatMessageRequestDTO messageRequest, Long fromIdx, Long toIdx) {
        // 송신자 채팅방 메시지
        ChatMessage userMessage = chatMessageRepository.save(ChatMessage.builder()
                .fromIdx(fromIdx)
                .toIdx(toIdx)
                .msg(messageRequest.getMsg())
                .roomId(userRoomId)
                .isRead(false)
                .build());

        // 수신자 채팅방 메시지
        chatMessageRepository.save(ChatMessage.builder()
                .fromIdx(fromIdx)
                .toIdx(toIdx)
                .msg(messageRequest.getMsg())
                .roomId(otherRoomId)
                .isRead(false)
                .build());

        return userMessage;
    }

    public ChatMessageDTO setChatMessageDTO(Long userIdx, ChatMessage chatMessage) {
        if(Objects.equals(userIdx,chatMessage.getFromIdx())) {
            return ChatMessageUserDTO.builder()
                    .msg(chatMessage.getMsg())
                    .timestamp(chatMessage.getTimestamp())
                    .build();
        }
        if(Objects.equals(userIdx,chatMessage.getToIdx())) {
            return ChatMessageOtherDTO.builder()
                    .msg(chatMessage.getMsg())
                    .timestamp(chatMessage.getTimestamp())
                    .build();
        }
        return null;
    }

}
