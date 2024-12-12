package com.spot.fun.usr.chat.service;

import com.spot.fun.token.service.AuthTokenServiceImpl;
import com.spot.fun.usr.chat.dto.*;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatFacadeService {
  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;
  private final UserServiceImpl userService;
  private final AuthTokenServiceImpl authTokenService;

  // 현재 로그인한 유저의 채팅방 리스트 반환
  public List<ChatRoomListResponseDTO> getChatRoomList(){
    Long userIdx = authTokenService.getCurrentUserIdx();
    return chatRoomService.findAll(userIdx).stream()
            .map(
                    chatRoom -> chatRoomService.setChatRoomListResponseDTO(
                            chatMessageService.setChatRoomListResponseDTO(chatRoom.getRoomId())
                            ,userService.findByIdx(chatRoomService.getOtherIdx(chatRoom.getRoomId()))
                    )).collect(Collectors.toList());
  }

  // 특정 상대방과 나눈 채팅 메시지를 chatId로 오름차순 정렬하여 반환
  public TreeMap<Long, ChatMessageDTO> getChatIdChatMessageDTOMap(Long otherIdx){
    Long userIdx = authTokenService.getCurrentUserIdx();

    // userIdx와 otherIdx로 roomId 조회
    Long roomId = chatRoomService.getRoomId(userIdx, otherIdx);

    // chatId를 key로 하는 TreeMap 생성
    TreeMap<Long, ChatMessageDTO> chatIdChatMessageDTOMap = new TreeMap<>();

    // 해당 방의 모든 채팅 메시지를 처리
    chatMessageService.findChatIdListByRoomId(roomId)
            .forEach(chatId -> {
              ChatMessage chatMessage = chatMessageService.findChatMessageById(chatId);
              ChatMessageDTO messageDTO = chatMessageService.setChatMessageDTO(userIdx, chatMessage);
              chatIdChatMessageDTOMap.put(chatId, messageDTO);
            });
    return chatIdChatMessageDTOMap;
  }

  // 특정 상대방과의 채팅방 입장 시 브라우저에 표현될, 채팅방 레이아웃을 구성할 데이터를 반환
  public ChatRoomContentDTO getChatRoomContentDTO(Long otherId){
    UserDTO userDTO = userService.findByIdx(otherId);
    return ChatRoomContentDTO.builder()
            .otherNickname(userDTO.getNickname())
            .otherProfileImg("")
            .otherPeedUrl("")
            .build();
  }

  public RoomIdPairDTO getOrCreateChatRooms(Long otherIdx) {
    Long userIdx = authTokenService.getCurrentUserIdx();

    // 각각의 채팅방 ID 조회
    Long userRoomId = chatRoomService.getRoomId(userIdx, otherIdx);
    Long otherRoomId = chatRoomService.getRoomId(otherIdx, userIdx);

    // 채팅방이 없으면 생성
    if (userRoomId == null && otherRoomId == null) {
      RoomIdPairDTO newRooms = chatRoomService.createChatRooms(userIdx, otherIdx);
      userRoomId = newRooms.getUserRoomId();
      otherRoomId = newRooms.getOtherRoomId();
    }

    return RoomIdPairDTO.builder()
            .userRoomId(userRoomId)
            .otherRoomId(otherRoomId)
            .build();
  }

  public ChatMessage saveChatMessage(Long roomId, Long otherRoomId, ChatMessageRequestDTO messageRequest) {
    Long userIdx = authTokenService.getCurrentUserIdx();
    Long otherIdx = chatRoomService.getOtherIdx(roomId);

    return chatMessageService.saveMessageForBothRooms(roomId, otherRoomId, messageRequest, userIdx, otherIdx);
  }


}
