// pages/ChatListPage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate, Outlet } from 'react-router-dom';
import { chatApi } from '../api/chatApi';
import { useAuth } from '../../../common/hook/useAuth';

const ChatListPage = () => {
    const { isAuthenticated, user } = useAuth();
    const [chatRooms, setChatRooms] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                if (!isAuthenticated) {
                    navigate('/login');
                    return;
                }

                const response = await chatApi.getChatRoomList();
                setChatRooms(response.data.chatRoomList);
            } catch (error) {
                if (error.response?.status === 401) {
                    navigate('/login');
                } else {
                    setError('채팅방 목록을 불러오는데 실패했습니다.');
                    console.error('채팅방 목록 조회 실패:', error);
                }
            }
        };

        fetchChatRooms();
    }, [isAuthenticated, navigate]);

    if (error) {
        return <div className="p-4 text-red-500">{error}</div>;
    }

    const handleRoomClick = (otherIdx) => {
        navigate(`/chat/${otherIdx}`);
    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">채팅 목록</h1>
            <div className="space-y-2">
                {chatRooms.map((room) => (
                    <div
                        key={room.otherIdx}
                        onClick={() => handleRoomClick(room.otherIdx)}
                        className="p-4 border rounded-lg cursor-pointer hover:bg-gray-50"
                    >
                        <div className="flex items-center gap-4">
                            <img
                                src={room.otherProfileImg || '/default-avatar.png'}
                                alt="프로필"
                                className="w-12 h-12 rounded-full"
                            />
                            <div>
                                <h3 className="font-medium">{room.otherNickname}</h3>
                                <p className="text-gray-600">{room.recentMessage}</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <Outlet />
        </div>
    );
};

export default ChatListPage;