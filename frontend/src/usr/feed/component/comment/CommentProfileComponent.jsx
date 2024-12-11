import { useState } from "react";
import { useBasic } from "../../../../common/context/BasicContext";
import InputComponent from "./InputComponent";
import { useNavigate } from "react-router-dom";

const CommentProfileComponent = ({
  comment,
  handleReplyClick,
  handleCommentModifyEvent,
  handleCommentDeleteEvent,
}) => {
  const navigate = useNavigate();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  const [activeComment, setActiveComment] = useState(null);
  const handleCommentModifyClick = (commentId) => {
    setActiveComment((prev) => (prev === commentId ? null : commentId));
  };

  const handleCommentEvent = (content) => {
    handleCommentModifyEvent({
      idx: comment.idx,
      content,
    });
    setActiveComment(null);
  };

  const handleProfileEvent = (userIdx) => {
    //console.log("사용자 idx, 마이페이지 이동처리 필요 시 사용 >> " + userIdx);
    if (loginUserIdx !== "" && loginUserIdx !== userIdx) {
      navigate(`/mypage/feed/${userIdx}`);
    }
  };

  return (
    <div className="flex space-x-3">
      <img
        src=""
        alt="프로필 이미지"
        className={`
          ${
            loginUserIdx !== "" && loginUserIdx !== comment.user.idx
              ? "cursor-pointer"
              : ""
          }
          w-10 h-10 rounded-full object-cover`}
        onClick={() => handleProfileEvent(comment.user.idx)}
      />
      <div className="flex flex-col w-full">
        <div className="flex justify-between items-center">
          <p
            className={`
              ${
                loginUserIdx !== "" && loginUserIdx !== comment.user.idx
                  ? "cursor-pointer"
                  : ""
              }
              font-semibold text-gray-800`}
            onClick={() => handleProfileEvent(comment.user.idx)}
          >
            {comment.user.nickname}
          </p>
          <div className="flex text-xs text-gray-500 items-center">
            {comment.user.idx === loginUserIdx && (
              <>
                <button
                  className="text-blue-500"
                  onClick={() => handleCommentModifyClick(comment.idx)}
                >
                  수정
                </button>
                <span className="mx-2 text-gray-500">|</span>
                <button
                  className="text-red-500"
                  onClick={() => handleCommentDeleteEvent(comment.idx)}
                >
                  삭제
                </button>
              </>
            )}
          </div>
        </div>

        {/* 수정 모드일 경우, 인풋과 버튼을 보여줌 */}
        {activeComment && activeComment === comment.idx ? (
          <div>
            <InputComponent
              handleCommentEvent={handleCommentEvent}
              content={comment.content}
            />
          </div>
        ) : (
          <p className="text-sm text-gray-600">{comment.content}</p> // 수정 모드가 아닐 때 원래 댓글 내용
        )}

        {/* <p className="text-sm text-gray-600">{comment.content}</p> */}
        <div
          className={`mt-1 flex text-xs text-gray-500 
          ${loginUserIdx ? "justify-between" : "justify-end"}`}
        >
          {/* 좌측: 답글 달기 버튼 */}
          {loginUserIdx && (
            <button
              className="text-blue-500"
              onClick={() => handleReplyClick(comment.idx)}
            >
              답글달기
            </button>
          )}

          {/* 우측: 등록일 */}
          <span className="">{comment.regDateStr}</span>
        </div>
      </div>
    </div>
  );
};

export default CommentProfileComponent;
