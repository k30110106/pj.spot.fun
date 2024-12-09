import axios from 'axios';

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.withCredentials = true; // 쿠키사용여부 설정

// 채팅방 목록 조회
export const chatroomListApi = async () => {
    // const config = {
    //     params: loginUserDTO,
    // };
    const res = await axios.get(`/api/chat/`);
    return res.data;
}

// 채팅방 입장(= 채팅 내역 조회 & 채팅방 구성 조회)
export const chatroomApi = async (otherIdx) => {
    const res = await axios.get(`/api/chat/${otherIdx}`);
    return res.data;
}