package com.spot.fun.chat.controller;

import com.spot.fun.chat.service.ChatRoomService;
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

    @GetMapping("/")
    public Map<String, Object> getChatRoomList() {
        return Map.of("message", "hello@@@@");
    }

    @GetMapping("/{userId}")
    public Map<String, Object> getChatListOfUserId(@PathVariable("userId") Long userId, @PathVariable("roomUserId") Long roomUserId) {
        return Map.of("message", "hello@@@@");
    }

    @PostMapping("/{userId}")
    public Map<String, Object> postChat(@PathVariable("userId") Long userId) {
        return Map.of("message", "hello@@@@");
    }
}
