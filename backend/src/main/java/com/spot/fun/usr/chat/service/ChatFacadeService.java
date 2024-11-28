package com.spot.fun.usr.chat.service;

import com.spot.fun.usr.chat.dto.ChatMessageDTO;
import com.spot.fun.usr.chat.dto.ChatRoomContentDTO;
import com.spot.fun.usr.chat.dto.ChatRoomResponseDTO;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.chat.entity.ChatRoom;
import com.spot.fun.usr.user.service.UserService;
import com.spot.fun.usr.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatFacadeService {
  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;
  private final UserServiceImpl userServiceImpl;

  // ChatRoomDTO 리스트 반환
  // controller 에서 Map<String, Object> = "chatRoomDTOList" : List<ChatRoomResponseDTO> 만들어서 반환할거임
  public List<ChatRoomResponseDTO> getChatRoomList(){
    // 1. 현재 사용자 idx를 추출 (현재 접속한 사용자를 어떻게 알아낼 수 있을까?)
    // 2. tbl_chatroom.useridx = idx인 List<ChatRoom>를 추출
    // 3. List<ChatRoom>을 순회하는데
    // 3-1. ChatRoom의 roomId를 추출
    // 3-2. tbl_chatroom.roomId = ChatRoom.roomId인 List<ChatMessage> 객체를
    //      timestamp 기준 최신순으로 정렬하고 가장 최근에 생성된 ChatMessage 객체 추출
    // 3-3. ChatRoomResponseDTO.recentMessage = ChatMessage.msg,
    //      ChatRoomResponseDTO.recentMessageTimestamp = ChatMessage.timestamp,
    //      ChatRoomResponseDTO.isRecentMessageRead = ChatMessage.isRead
    //      위와 같은 작업으로 최근 메시지 문자열, 최근 타임스탬프, 최근 메시지 읽음 여부 세팅
    // 3-4. 이제 otherNickname, otherProfileImg, otherPeedUrl 세팅해야하는데,
    //      tbl_chatroom.roomId = ChatRoom.roomId ChatRoom 객체의 otherIdx 추출
    // 3-5. ChatRoom.otherIdx = tbl_user.idx인 User 객체를 추출
    // 3-6. ChatRoomResponseDTO.otherNickname = User.nickname,
    //      ChatRoomResponseDTO.otherProfileImg = User.profileImg(아직 없어서 그냥 ""로),
    //      ChatRoomResponseDTO.otherPeedUrl = User.otherPeedUrl(아직 없어서 그냥 ""로)
    //      위와 같은 작업으로 other 정보 세팅
    // 3-7. List<ChatRoomResponseDTO>.add(ChatRoomResponseDTO)
    // 4. 반복문이 완료되면 return

    Long userIdx = 0L;  // 현재 사용자 idx 추출 어떻게 할 지 몰라서 임의로 설정
    return chatRoomService.findAll(userIdx).stream()
            .map(
                    chatRoom -> chatRoomService.setChatRoomResponseDTO(
                            chatMessageService.setChatRoomResponseDTO(chatRoom.getRoomId())
                            ,userServiceImpl.findByIdx(chatRoomService.getOtherIdx(chatRoom.getRoomId())))
            ).collect(Collectors.toList());
  }

  // Map<Long chat_id, ChatMessageDTO> 반환
  // controller 에서 Map<String, Object> = "chatIdChatMessageDTOMap" :  Map<Long chat_id, ChatMessageDTO> 만들어서 반환할거임
  // 맞다 채팅방 틀에 넣을 데이터 DTO도 넣어야함... 이건 그냥 메서드를 따로 만들자
  public TreeMap<Long, ChatMessageDTO> getChatIdChatMessageDTOMap(Long otherIdx){
    // 1. 현재 사용자 idx를 추출 (현재 접속한 사용자를 어떻게 알아낼 수 있을까?)
    // 2. idx와 otherId로 roomId를 추출
    //    chatRoomService를 사용함
    // 3. roomId로 chatId 리스트를 추출
    //    chatMessageService를 사용함
    // 4. List<Long chatIdList>를 순회하는데,
    // 4-1. chatId로 ChatMessage 객체를 추출함
    //      chatMessageService를 사용함
    // 4-2. userIdx가 ChatMessage 객체의 fromId인지, toId인지 검사함
    // 4-3. 만약 userIdx가 fromId라면 현재 사용자는 송신자. 사용자는 ChatMessage를 보낸 사람
    //      브라우저에 표현될 정보는 msg와 timestamp => ChatMessageUserDTO에 세팅
    //      만약 userIdx가 toId라면 현재 사용자는 수신자. 사용자는 ChatMessage를 받은 사람
    //      브라우저에 표현될 정보는 msg, timestamp, otherNickname => ChatMessageOtherDTO에 세팅
    //      이 작업은 chatMessageService를 사용함
    // 5. 세팅된 chatMessageDTO를 userIdx와 함께 TreeMap에 저장
    // 6. 순회가 끝나면 반환

    Long userIdx = 0L;  // 현재 사용자 idx 추출 어떻게 할 지 몰라서 임의로 설정
    List<ChatMessageDTO> chatMessageDTOList = chatMessageService.findChatIdByRoomId(chatRoomService.getRoomId(userIdx, otherIdx)).stream()
            .map(chatId -> {
              return chatMessageService.setChatMessageDTO(userIdx, chatMessageService.findChatMessageById(chatId));
              }).toList();
    chatRoomService.getRoomId(userIdx, otherIdx);
    return null;

  }

  // otherId와의 채팅방 입장 시 브라우저에 표현될 채팅방 프레임 content를 담은 DTO 반환
  // 예) 상대방 닉네임, 상대방 프로필 이미지, 상대방 피드 url ...
  public ChatRoomContentDTO getChatRoomContentDTO(Long otherId){
    return null;
  }

  // post는 반환값을 뭐로해야하지?
  public Map<String, Object> postChatMessage(ChatMessage chatMessage){
    return null;
  }
}
