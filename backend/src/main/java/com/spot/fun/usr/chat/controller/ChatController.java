package com.spot.fun.usr.chat.controller;

import com.spot.fun.usr.chat.service.ChatFacadeService;
import com.spot.fun.usr.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    ChatRoomService chatRoomService;

    private final ChatFacadeService chatFacadeService;

    @GetMapping("/")    //채팅방 리스트
    public Map<String, Object> getChatRoomList() {
        return Map.of("chatRoomList", chatFacadeService.getChatRoomList());
    }

    @GetMapping("/{otherId}")    // 상대방이 userId인 채팅 메시지 리스트 + 채팅방 레이아웃 구성을 위한 DTO
    public Map<String, Object> getChatListOfUserId(@PathVariable("otherId") Long otherId) {
        return Map.of(
                "chatIdChatMessageDTOMap", chatFacadeService.getChatIdChatMessageDTOMap(otherId)
                ,"chatRoomContentDTO", chatFacadeService.getChatRoomContentDTO(otherId));
    }

    @PostMapping("/{userId}")   // 채팅 입력
    public Map<String, Object> postChat(@PathVariable("userId") Long userId) {
        return Map.of("message", "hello@@@@");
    }
}
