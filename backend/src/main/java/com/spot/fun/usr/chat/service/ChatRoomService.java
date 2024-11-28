package com.spot.fun.usr.chat.service;

import com.spot.fun.usr.chat.dto.ChatRoomResponseDTO;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.chat.entity.ChatRoom;
import com.spot.fun.usr.chat.repository.ChatMessageRepository;
import com.spot.fun.usr.chat.repository.ChatRoomRepository;
import com.spot.fun.usr.user.dto.UserDTO;
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
                                .otherNickname(chatRoom.getOther().getUsername())
                                .recentMessage(recentMessage.getMsg())
                                .recentMessageTimestamp(recentMessage.getTimestamp())
                                .isRecentMessageRead(recentMessage.isRead())
                                .build();
                    }
                    else {
                        return ChatRoomResponseDTO.builder()
                                .otherNickname(chatRoom.getOther().getUsername())
                                .recentMessage("이 채팅방에 아직 메시지가 없습니다")
                                .recentMessageTimestamp(null)
                                .isRecentMessageRead(false)
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    public List<ChatRoom> findAll(Long userIdx) {
      return chatRoomRepository.findAllByUserIdx(userIdx);
    }

    public Long getOtherIdx(Long roomId) {
      return chatRoomRepository.findOtherIdByRoomId(roomId);
    }

    public ChatRoomResponseDTO setChatRoomResponseDTO(ChatRoomResponseDTO chatRoomResponseDTO, UserDTO userDTO) {
      return chatRoomResponseDTO.toBuilder()
              .otherNickname(userDTO.getNickname())
              .otherProfileImg("userDTO.getProfileImg")
              .otherPeedUrl("userDTO.getFeedUrl")
              .build();
    }

    public Long getRoomId(Long userIdx, Long otherIdx) {
      return chatRoomRepository.findRoomIdByUserIdxAndOtherIdx(userIdx, otherIdx);
    }

    // 채팅방 개설(사용자가 메시지를 보내거나 받으면 user chatroom, other chatroom 생성)
//    public List<ChatRoom> createChatRoom(ChatRoomRequestDTO chatRoomRequestDTO) {
//
//
//    }



}
