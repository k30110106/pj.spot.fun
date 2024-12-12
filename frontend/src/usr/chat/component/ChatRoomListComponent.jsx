import { useEffect, useState } from "react";
import { chatroomApi } from "../api/chatroomApi";
import ChatOtherProfileComponent from "./ChatOtherProfileComponent";

// 초기화
const initState = {
    // 빈 배열로 초기화
    chatRoomListResponseDTOs: [],
};

const ChatRoomListItemComponent = () => {
    const [chatRoomListResponseDTOs, setChatRoomListResponseDTOs] = useState(initState);

    // 컴포넌트가 마운트될 때 한 번만 실행됨 > []라서
    useEffect(() => {
        chatroomApi()
            .then((data) => {
                console.log(data);
                setChatRoomListResponseDTOs(data);
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);
    return(
        <>
            {chatRoomListResponseDTOs.map((chatRoomListResponseDTO) => (
                <div key={chatRoomListResponseDTO.roomId}>
                    <ChatOtherProfileComponent />
                    <div id="chatroom-text-container">
                        <p>{chatRoomListResponseDTO.otherNickname}</p>
                        <p>최근 대화 : {chatRoomListResponseDTO.recentMessage}</p>
                    </div>
                    {chatRoomListResponseDTO.isRecentMessageRead ? <></> : <img alt="unCheckedSign"/> }
                </div>
            ))};
        </>
    );
};

export default ChatRoomListItemComponent;