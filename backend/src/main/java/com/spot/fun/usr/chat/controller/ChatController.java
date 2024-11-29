package com.spot.fun.usr.chat.controller;

import com.spot.fun.usr.chat.dto.ChatMessageRequestDTO;
import com.spot.fun.usr.chat.entity.ChatMessage;
import com.spot.fun.usr.chat.service.ChatFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatFacadeService chatFacadeService;

    @GetMapping("/")    //채팅방 리스트
    public Map<String, Object> getChatRoomList() {
        return Map.of("chatRoomList", chatFacadeService.getChatRoomList());
    }

    @GetMapping("/{otherIdx}")    // 상대방이 userId인 채팅 메시지 리스트 + 채팅방 레이아웃 구성을 위한 DTO
    public Map<String, Object> getChatListOfOtherIdx(@PathVariable("otherIdx") Long otherIdx) {
        // 채팅 내역이 있는 채팅방에 입장하는 상황만을 염두하고 만든 코드인데 문제가 있다.
        // 채팅을 보내거나 받은 시점에 채팅방이 생성(chatroom 엔티티 생성)되게 하려했는데,
        // 채팅메시지 레코드가 생성된 후에 채팅방 레코드가 생성되는건 애초에 불가능함. 말이 안됨.
        // 그러니
        // 현재 사용자가 특정 사용자 피드의 DM버튼을 처음 클릭하거나, 누군가 현재 사용자의 DM버튼을 처음 클릭했을 때
        // 그러니까 chatroom 테이블에 userIdx-otherIdx인 레코드가 없다면 채팅방 레코드를 생성하도록 해야함.
        // 나든 상대방이든 DM버튼이 눌렸을 때 chatroom테이블의 userIdx=나 otherIdx=상대방인 roomId가 있는지 확인하는데,
        // 만약 위 조건을 만족하는 roomId가 없는데, otherIdx=나 userIdx=상대방인 roomId는 있다면,
        // 나랑 상대방이 거의 동시에 DM버튼을 눌렀는데, 간발의 차로 상대가 먼저 DM버튼을 누른게 되는거임
        // 왜냐면 채팅방이 없으면 chatroom - userIdx=나, otherIdx=상대방 / userIdx=상대방, otherIdx=나
        // 이렇게 두개의 레코드를 만들거니까.
        // 이유는 메시지 삭제 기능이 추가될지도 모르니까.
        // 메시지 삭제는 상대방한테까지 안보이게 지우는게 아니라 내 화면에만 안보이게끔 해야하니까.

        // 여튼 @MessageMapping(채팅 입력)이 호출되기 전에 chatroom이 이미 만들어져있어야하기 때문에
        // 위와같은 작업이 선행되어야한다는 말임
        return Map.of(
                "chatIdChatMessageDTOMap", chatFacadeService.getChatIdChatMessageDTOMap(otherIdx)
                ,"chatRoomContentDTO", chatFacadeService.getChatRoomContentDTO(otherIdx));
    }

//    @PostMapping("/{otherIdx}")   // 채팅 입력
//    public Map<String, Object> postChat(@PathVariable("otherIdx") Long otherIdx) {
//        return Map.of("message", "hello@@@@");
//    }

    @MessageMapping("/{otherIdx}")
    @SendTo("sub/{userIdx}")
    public ChatMessage postChatMessage(@PathVariable("otherIdx") Long otherIdx, @RequestBody ChatMessageRequestDTO chatMessageRequestDTO) {
        // ChatFacadeService에 createChatMessage 메서드가 있어야함
        // 1. requestBody로 ChatMessageRequestDTO를 받음
        // 2. chatmsg DM에 저장하는데,
        // 2. 현재 사용자가 chatroom의 userIdx이고 상대방이 otherIdx인 roomId를 추출하고,
        // 2-1. chatmsg 테이블에 해당 roomId와 ChatMessageRequestDTO값을 세팅해서 DB에 저장
        // 3. 현재 사용자가 chatroom의 otherIdx이고 상대방이 userIdx인 roomId를 추출하고,
        // 3-1. chatmsg 테이블에 해당 roomId와 ChatMessageRequestDTO값을 세팅해서 DB에 저장
        // 근데 지금 깨달은건데... 학습일지에 링크걸어논 벨로그 채팅방 보니까...
        // 굳이 내가 보낸 채팅, 내가 받은 채팅 DTO 필드를 다르게 세팅할 이유가 없어보임
        // 어차피 1대1 채팅인데 내가 받은 채팅메시지 위에 상대방 닉네임이 표현돼야할 이유가 없음...
        // 또, 나한테 표현되는건 받은 메시지값, 즉 ChatOtherDTO이고 상대에게 표현되는건 보낸 메시지값 ChatUserDTO인데
        // 그러면 return문이 결국 두개 필요하게 되는거 아님?
        // 뭔말이냐면 @MessageMapping("/{otherIdx}") @SendTo("sub/{userIdx}")이 공존하는 메서드를 만들수가 없단거
        // 근데 애초에 같은 채팅멤버를 갖는 chatroom 레코드를 나-상대방, 상대방-나로 두개씩 만드는 상황에서
        // 상대방이 입장한 채팅방은 상대방이 userIdx로 세팅된 roomId이고 내가 입장한 채팅방은 내가 userIdx인 roomId인데,
        // 내가 내 채팅의 sub이 되어야 내가 보낸 메시지도 내 채팅방에 표현되는거 아닌가?
        // 채팅을 보낼 때마다 DB를 갔다오면 웹소켓을 쓰는 이유가 뭐냐?
        // DB는 채팅방 입장했을 때, 지금껏 나눈 채팅기록 가져올 때, 그 때 한번만 갔다와야지...
        // 여튼 뜯어고쳐야할듯
        // 근데 그 벨로그 작성자분 깃허브 보니까 상대방 이름 뜨게 나오는디
        // 아마 이름도 같이 보내는거같음. 다만 내가 송신자인 경우에는 웹브라우저에 내 이름 표현 안하는듯
        // 여튼 요청이 /{otherIdx}가 아니라 /{roomId}여야할거같은데
        // 그럼 pk를 [roomId, userIdx, otherIdx]로 설정해야하나?
        // 내가 보낸 채팅에 대해서는 따로 sub/이 필요가 없었다.
        // => 왜냐면 굳이 백에다 요청, 응답, 후 표현하지 않아도, 클라 단에서 바로 표현 가능한 일이었음..
//        ChatMessage chatMessageForUser = chatFacadeService.createChatMessageForUser(chatMessageRequestDTO);
//        ChatMessage chatMessageForOther = chatFacadeService.createChatMessageForOther(chatMessageRequestDTO);
        return null;
    }
}
