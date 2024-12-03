// api/chatApi.js
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

let stompClient = null;

export const chatApi = {
    // WebSocket 연결 설정
    connectWebSocket: (userRoomId, otherRoomId, onMessageReceived) => {
        const socket = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            // 자신의 채팅방 구독
            stompClient.subscribe(`/sub/user/${userRoomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                onMessageReceived(receivedMessage);
            });

            // 상대방의 채팅방 구독
            stompClient.subscribe(`/sub/other/${otherRoomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                onMessageReceived(receivedMessage);
            });
        });
    },

    // 채팅방 목록 조회
    getChatRoomList: async () => {
        const response = await fetch('/api/chat/');
        return response.json();
    },

    // 특정 사용자와의 채팅 내역 조회
    getChatRoom: async (otherIdx) => {
        const response = await fetch(`/api/chat/${otherIdx}`);
        return response.json();
    },

    // 메시지 전송
    sendMessage: (roomId, otherRoomId, message) => {
        if (stompClient) {
            stompClient.send(`/app/chat/${roomId}`, {}, JSON.stringify({
                roomId,
                otherRoomId,
                msg: message
            }));
        }
    },

    // WebSocket 연결 종료
    disconnect: () => {
        if (stompClient) {
            stompClient.disconnect();
        }
    }
};