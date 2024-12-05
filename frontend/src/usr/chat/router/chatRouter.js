// usr/chat/router/chatRouter.js
import { lazy } from "react";

const ChatListPage = lazy(() => import("../../chat/page/ChatListPage"));
const ChatRoomPage = lazy(() => import("../../chat/page/ChatRoomPage"));

const chatRouter = () => [
    {
        path: "",
        element: <ChatListPage />,
        children: [
            {
                path: ":otherIdx",
                element: <ChatRoomPage />
            }
        ]
    }
];

export default chatRouter;
