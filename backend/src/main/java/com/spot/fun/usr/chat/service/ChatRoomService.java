package com.spot.fun.usr.chat.service;

import com.spot.fun.usr.chat.dto.ChatRoomListResponseDTO;
import com.spot.fun.usr.chat.entity.ChatRoom;
import com.spot.fun.usr.chat.repository.ChatRoomRepository;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 채팅방 조회, 채팅방 삭제, 채팅방 전체 리스트 조회 로직
@Service
@RequiredArgsConstructor
public class ChatRoomService implements ChatService{
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAll(Long userIdx) {
      return chatRoomRepository.findAllByUserIdx(userIdx);
    }

    public Long getOtherIdx(Long roomId) {
      return chatRoomRepository.findOtherIdByRoomId(roomId);
    }

    public ChatRoomListResponseDTO setChatRoomListResponseDTO(ChatRoomListResponseDTO chatRoomListResponseDTO, UserDTO userDTO) {
      return chatRoomListResponseDTO.toBuilder()
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
