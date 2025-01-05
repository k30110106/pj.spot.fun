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
public class ChatMessageService{
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomListResponseDTO setChatRoomListResponseDTO(Long roomId) {
        ChatMessage recentChatMessage = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(roomId);
//        System.out.println("setChatRoomListResponseDTO recentChatMessage: " + recentChatMessage);
        if(Objects.isNull(recentChatMessage)) {
            return ChatRoomListResponseDTO.builder()
                    .roomId(roomId)
                    .build();
        }
        return ChatRoomListResponseDTO.builder()
                .roomId(roomId)
                .recentMessage(recentChatMessage.getMsg())
                .recentMessageTimestamp(recentChatMessage.getTimestamp())
                .isRecentMessageRead(recentChatMessage.isRead())
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
        // 송신자 채팅방 메시지 - 내가 보낸 메시지는 항상 읽음 처리
        ChatMessage userMessage = chatMessageRepository.save(ChatMessage.builder()
                .fromIdx(fromIdx)
                .toIdx(toIdx)
                .msg(messageRequest.getMsg())
                .roomId(userRoomId)
                .isRead(true)  // 내가 보낸 메시지는 항상 읽음 처리
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

    // return 타입을 ChatMessageResponseDTO로 통일
    public ChatMessageResponseDTO toChatMessageResponseDTO(ChatMessage chatMessage) {
        return ChatMessageResponseDTO.builder()
                .fromIdx(chatMessage.getFromIdx())
                .msg(chatMessage.getMsg())
                .timestamp(chatMessage.getTimestamp())
                .isRead(chatMessage.isRead())  // isRead 필드 추가
                .build();
    }

    // 채팅방의 모든 메시지를 읽음 처리
    public void markMessagesAsRead(Long roomId, Long toIdx) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findByRoomIdAndToIdxAndIsReadFalse(roomId, toIdx);
        unreadMessages.forEach(message -> {
            message.markAsRead();
            chatMessageRepository.save(message);
        });
    }
}
