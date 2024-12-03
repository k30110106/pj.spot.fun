package com.spot.fun.usr.chat.service;

import com.spot.fun.usr.chat.dto.ChatRoomListResponseDTO;
import com.spot.fun.usr.chat.dto.RoomIdPairDTO;
import com.spot.fun.usr.chat.entity.ChatRoom;
import com.spot.fun.usr.chat.repository.ChatRoomRepository;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 채팅방 조회, 채팅방 삭제, 채팅방 전체 리스트 조회 로직
@Service
@RequiredArgsConstructor
public class ChatRoomService implements ChatService{
    private final ChatRoomRepository chatRoomRepository;
    private final UserServiceImpl userService;


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

    // 양방향 채팅방 생성
    public RoomIdPairDTO createChatRooms(Long userIdx, Long otherIdx) {
      User user = userService.findUserByIdx(userIdx)
              .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
      User other = userService.findUserByIdx(otherIdx)
            .orElseThrow(() -> new IllegalArgumentException("상대방을 찾을 수 없습니다."));

      // user -> other 채팅방
      ChatRoom userChatRoom = chatRoomRepository.save(ChatRoom.builder()
              .user(user)
              .other(other)
              .build());

      // other -> user 채팅방
      ChatRoom otherChatRoom = chatRoomRepository.save(ChatRoom.builder()
              .user(other)
              .other(user)
              .build());

      return RoomIdPairDTO.builder()
              .userRoomId(userChatRoom.getRoomId())
              .otherRoomId(otherChatRoom.getRoomId())
              .build();
    }
}
