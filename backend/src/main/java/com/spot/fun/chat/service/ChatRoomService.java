package com.spot.fun.chat.service;

import com.spot.fun.chat.dto.ChatRoomRequestDTO;
import com.spot.fun.chat.dto.ChatRoomResponseDTO;
import com.spot.fun.chat.entity.ChatMessage;
import com.spot.fun.chat.entity.ChatRoom;
import com.spot.fun.chat.repository.ChatMessageRepository;
import com.spot.fun.chat.repository.ChatRoomRepository;
import com.spot.fun.usr.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 채팅방 조회, 채팅방 삭제, 채팅방 전체 리스트 조회 로직
@Service
@RequiredArgsConstructor
public class ChatRoomService implements ChatService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 내 채팅방 전체 리스트 조회
    public List<ChatRoomResponseDTO> findAllByUser(User user) {
        return chatRoomRepository.findChatRoomByUser(user).stream()
                .map(chatRoom -> {
                    ChatMessage recentMessage = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(chatRoom.getRoomId());
                    if (recentMessage!=null) {
                        return ChatRoomResponseDTO.builder()
                                .roomId(chatRoom.getRoomId())
                                .otherUserName(chatRoom.getOther().getUsername())
                                .recentMessage(recentMessage.getMsg())
                                .recentMessageTimestamp(recentMessage.getTimestamp())
                                .recentMessageRead(recentMessage.isChecked())
                                .build();
                    }
                    else {
                        return ChatRoomResponseDTO.builder()
                                .roomId(chatRoom.getRoomId())
                                .otherUserName(chatRoom.getOther().getUsername())
                                .recentMessage("이 채팅방에 아직 메시지가 없습니다")
                                .recentMessageTimestamp(null)
                                .recentMessageRead(false)
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

//    // 채팅방 개설(사용자가 메시지를 보내거나 받으면 user chatroom, other chatroom 생성)
//    public List<ChatRoom> createChatRoom(ChatRoomRequestDTO chatRoomRequestDTO) {
//
//
//    }



}
