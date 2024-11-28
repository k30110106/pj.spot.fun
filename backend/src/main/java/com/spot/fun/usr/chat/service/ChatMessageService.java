package com.spot.fun.usr.chat.service;

import com.spot.fun.usr.chat.dto.*;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.chat.repository.ChatMessageRepository;
import com.spot.fun.usr.chat.repository.ChatRoomRepository;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

// 채팅방 메시지 저장하는 로직
@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatService{
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅 전송
    public void save(ChatMessageRequestDTO chatMessageRequestDTO) {
        ChatMessage chatMessage = chatMessageRequestDTO.toEntity();
        chatMessageRepository.save(chatMessage);
    }

    // 특정 채팅방의 채팅 내역 조회(Map<Long chat_id, ChatMessageDTO>)
    // 예외처리 해야하는디... 귀찮으니 뒤로 미룸
    public TreeMap<Long, ChatMessageDTO> findChatList(Long roomId) {
        List<Long> chatIdList = chatMessageRepository.findChatIdByRoomId(roomId);

        TreeMap<Long, ChatMessageDTO> chatMessageDTOMap = new TreeMap<>();
        for (Long chatId : chatIdList) {
            Optional<ChatMessage> chatMessage = chatMessageRepository.findByChatId(chatId);
            // Optional객체가 null이 아니라면
            if (chatMessage.isPresent()) {
                // 현재 사용자가 송신자인지 수신자인지 체크하기 위해 roomId로 현재 사용자인 userId 추출
                Long userId = chatRoomRepository.findUserIdByRoomId(roomId);
                Long otherId = chatRoomRepository.findOtherIdByRoomId(roomId);
                // 현재 사용자가 송신자인 경우(= 메시지를 보낸 경우)
                if(Objects.equals(userId, chatMessageRepository.findFromIdByChatId(chatId))) {
                    chatMessageDTOMap.put(chatId, ChatMessageUserDTO.builder()
                            .msg(chatMessage.get().getMsg())
                            .timestamp(chatMessage.get().getTimestamp())
                            .build());
                    continue;
                }
                // 현재 사용자가 수신자인 경우(= 메시지를 받은 경우)
                if(Objects.equals(userId, chatMessageRepository.findToIdByChatId(chatId))) {
                    chatMessageDTOMap.put(chatId, ChatMessageOtherDTO.builder()
                            .otherName(otherId.toString())
                            .msg(chatMessage.get().getMsg())
                            .timestamp(chatMessage.get().getTimestamp())
                            .build());
                }
            }
            else {
                chatMessageDTOMap.put(chatId, null);
            }
        }
        return chatMessageDTOMap;
    }
    public ChatRoomResponseDTO setChatRoomResponseDTO(Long roomId) {
        ChatMessage recendChatMessage = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(roomId);
        return ChatRoomResponseDTO.builder()
                .recentMessage(recendChatMessage.getMsg())
                .recentMessageTimestamp(recendChatMessage.getTimestamp())
                .isRecentMessageRead(recendChatMessage.isRead())
                .build();
    }

    public List<Long> findChatIdByRoomId(Long roomId) {
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

}
