import { useState } from "react";
import { MdSubdirectoryArrowRight } from "react-icons/md";
import InputComponent from "./InputComponent";
import { useBasic } from "../../../../common/context/BasicContext";
import { API_BASE_URL } from "../../api/FeedApi";
import { useNavigate } from "react-router-dom";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ReplyListComponent = ({
  parentIdx,
  replyList,
  handleReplyModifyEvent,
  handleReplyDeleteEvent,
}) => {
  const navigate = useNavigate();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  const [activeComment, setActiveComment] = useState(null);
  const handleCommentClick = (commentId) => {
    setActiveComment((prev) => (prev === commentId ? null : commentId));
  };
  const handleCommentEvent = (content) => {
    handleReplyModifyEvent({
      idx: activeComment,
      parentIdx,
      content,
    });
    setActiveComment(null);
  };

  const handleProfileEvent = (userIdx) => {
    if (loginUserIdx !== "" && loginUserIdx !== userIdx) {
      navigate(`/mypage/feed/${userIdx}`);
    }
  };

  return (
    <>
      {/* 대댓글 (reply) */}
      <div className="pl-1 space-y-2">
        {replyList.map((reply) => (
          <div key={reply.idx} className="flex space-x-3">
            <MdSubdirectoryArrowRight
              className="text-gray-500 mt-1"
              size={16}
            />
            <img
              src={
                reply.user.uploadName
                  ? `${API_BASE_URL}/api/usr/profile/image/${reply.user.uploadName}`
                  : initSrc
              }
              alt="프로필 이미지"
              className={`
                 ${
                   loginUserIdx !== "" && loginUserIdx !== reply.user.userIdx
                     ? "cursor-pointer"
                     : ""
                 }
                w-8 h-8 rounded-full object-cover`}
              onClick={() => handleProfileEvent(reply.user.userIdx)}
            />
            <div className="flex flex-col w-full">
              <div className="flex justify-between items-center text-xs text-gray-500">
                <p
                  className={`
                    ${
                      loginUserIdx !== "" && loginUserIdx !== reply.user.userIdx
                        ? "cursor-pointer"
                        : ""
                    }
                    font-semibold text-gray-800`}
                  onClick={() => handleProfileEvent(reply.user.userIdx)}
                >
                  {reply.user.user.nickname}
                </p>
                <div className="flex items-center">
                  {reply.user.userIdx === loginUserIdx && (
                    <>
                      <button
                        className="text-blue-500"
                        onClick={() => handleCommentClick(reply.idx)}
                      >
                        수정
                      </button>
                      <span className="mx-2">|</span>
                      <button
                        className="text-red-500"
                        onClick={() =>
                          handleReplyDeleteEvent(reply.idx, parentIdx)
                        }
                      >
                        삭제
                      </button>
                    </>
                  )}
                </div>
              </div>

              {activeComment && activeComment === reply.idx ? (
                <div>
                  <InputComponent
                    handleCommentEvent={handleCommentEvent}
                    content={reply.content}
                  />
                </div>
              ) : (
                <p className="text-sm text-gray-600">{reply.content}</p> // 수정 모드가 아닐 때 원래 댓글 내용
              )}
              <div className="mt-1 flex justify-end text-xs text-gray-500">
                <span>{reply.regDateStr}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </>
  );
};

export default ReplyListComponent;
