// api/chatApi.js
// api/chatApi.js 수정
import axios from 'axios';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const API_BASE_URL = process.env.REACT_APP_API_ROOT;
let stompClient = null;

export const chatApi = {
    connectWebSocket: (userRoomId, otherRoomId, onMessageReceived) => {
        const socket = new SockJS(`${API_BASE_URL}/ws`);
        stompClient = Stomp.over(socket);

        // 토큰 추가
        const accessToken = localStorage.getItem("access_token");
        const headers = {
            Authorization: `Bearer ${accessToken}`
        };

        stompClient.connect(headers, () => {
            stompClient.subscribe(`/sub/user/${userRoomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                onMessageReceived(receivedMessage);
            });

            stompClient.subscribe(`/sub/other/${otherRoomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                onMessageReceived(receivedMessage);
            });
        });
    },

    getChatRoomList: async () => {
        const accessToken = localStorage.getItem("access_token");
        return await axios.get(`${API_BASE_URL}/api/chat/`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });
    },

    getChatRoom: async (otherIdx) => {
        const accessToken = localStorage.getItem("access_token");
        return await axios.get(`${API_BASE_URL}/api/chat/${otherIdx}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });
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