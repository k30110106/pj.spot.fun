import { useState } from "react";
import { useBasic } from "../../../../common/context/BasicContext";
import InputComponent from "./InputComponent";

const CommentProfileComponent = ({
  comment,
  handleReplyClick,
  handleCommentModifyEvent,
  handleCommentDeleteEvent,
}) => {
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";
  //console.log(loginUserIdx);

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

  return (
    <div className="flex space-x-3">
      <img
        src=""
        alt="프로필 이미지"
        className="w-10 h-10 rounded-full object-cover"
      />
      <div className="flex flex-col w-full">
        <div className="flex justify-between items-center">
          <p className="font-semibold text-gray-800">{comment.user.nickname}</p>
          <div className="flex text-xs text-gray-500 items-center">
            {comment.user.idx === loginUserIdx && (
              <>
                <button
                  className="text-blue-500"
                  onClick={() => handleCommentModifyClick(comment.idx)}
                >
                  수정
                </button>
                <span className="mx-2">|</span>
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
