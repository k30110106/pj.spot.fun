package com.spot.fun.usr.chat.controller;

import com.spot.fun.usr.chat.dto.ChatMessageRequestDTO;
import com.spot.fun.usr.chat.dto.ChatRoomListResponseDTO;
import com.spot.fun.usr.chat.dto.RoomIdPairDTO;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.chat.service.ChatFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatFacadeService chatFacadeService;

    @GetMapping("/")    //채팅방 리스트
//    public List<ChatRoomListResponseDTO> getChatRoomList() {
//        return chatFacadeService.getChatRoomList();y
    public Map<String, Object> getChatRoomList() {
        return Map.of("chatRoomList", chatFacadeService.getChatRoomList());
    }

    @GetMapping("/{otherIdx}")
    public Map<String, Object> getChatListOfOtherIdx(@PathVariable("otherIdx") Long otherIdx) {
        // 채팅방 조회 또는 생성 후, 두 개의 roomId 모두 반환
        RoomIdPairDTO roomIds = chatFacadeService.getOrCreateChatRooms(otherIdx);

        return Map.of(
                "userRoomId", roomIds.getUserRoomId(),      // 내 채팅방 ID
                "otherRoomId", roomIds.getOtherRoomId(),    // 상대방 채팅방 ID
                "chatIdChatMessageDTOMap", chatFacadeService.getChatIdChatMessageDTOMap(otherIdx),
                "chatRoomContentDTO", chatFacadeService.getChatRoomContentDTO(otherIdx)
        );
    }

    @MessageMapping("/chat/{roomId}")
    @SendToUser("/sub/user/{roomId}")
    @SendTo("/sub/other/{otherRoomId}")
    public ChatMessage handleChatMessage(@DestinationVariable("roomId") Long roomId,
                                         @DestinationVariable("otherRoomId") Long otherRoomId,
                                         @RequestBody ChatMessageRequestDTO messageRequest) {
        return chatFacadeService.saveChatMessage(roomId, otherRoomId, messageRequest);
    }
}
