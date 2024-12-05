import React, { useState } from "react";
import ReplyListComponent from "./ReplyListComponent";
import InputComponent from "./InputComponent";

const ListComponent = ({ commentList, handleReplyEvent }) => {
  console.log(commentList);

  const [activeReplyComment, setActiveReplyComment] = useState(null);
  const handleReplyClick = (commentId) => {
    // 답글 입력을 토글하는 역할
    setActiveReplyComment((prev) => (prev === commentId ? null : commentId));
  };

  const handleCommentEvent = (content) => {
    handleReplyEvent({ parentIdx: activeReplyComment, content: content });
    setActiveReplyComment(null);
  };

  return (
    <>
      {(commentList ?? []).length > 0 ? (
        commentList.map((comment) => (
          <div
            key={comment.idx}
            className="flex flex-col space-y-3 my-1 pb-4 border-b border-gray-200"
          >
            <div className="flex space-x-3">
              <img
                src=""
                alt="프로필 이미지"
                className="w-10 h-10 rounded-full object-cover"
              />
              <div className="flex flex-col w-full">
                <div className="flex justify-between items-center">
                  <p className="font-semibold text-gray-800">
                    {comment.user.nickname}
                  </p>
                  <div className="flex text-xs text-gray-500 items-center">
                    <button className="text-blue-500">수정</button>
                    <span className="mx-2">|</span>
                    <button className="text-red-500">삭제</button>
                  </div>
                </div>
                <p className="text-sm text-gray-600">{comment.content}</p>
                <div className="mt-1 flex justify-between text-xs text-gray-500">
                  {/* 좌측: 답글 달기 버튼 */}
                  <button
                    className="text-blue-500"
                    onClick={() => handleReplyClick(comment.idx)} // 실제 로직 추가
                  >
                    답글달기
                  </button>

                  {/* 우측: 등록일 */}
                  <span>{comment.regDateStr}</span>
                </div>
              </div>
            </div>

            {/* 답글 입력 영역 */}
            {activeReplyComment === comment.idx && (
              // <div className="mt-2 flex space-x-3">
              //   <input
              //     type="text"
              //     className="w-4/5 px-3 py-1 text-sm border border-gray-300 rounded-md"
              //     placeholder="답글을 입력하세요..."
              //     value={replyContent}
              //     onChange={(e) => setReplyContent(e.target.value)}
              //   />
              //   <button
              //     className="w-1/5 bg-blue-500 text-white px-2 py-1 rounded-md text-sm"
              //     onClick={() => handleSubmitReply(comment.idx)}
              //   >
              //     등록
              //   </button>
              // </div>
              <InputComponent handleCommentEvent={handleCommentEvent} />
            )}

            {(comment.replyList ?? []).length > 0 && (
              <ReplyListComponent replyList={comment.replyList} />
            )}
          </div>
        ))
      ) : (
        <div className="flex space-x-3 my-1">댓글이 없습니다</div>
      )}
    </>
  );
};

export default ListComponent;
