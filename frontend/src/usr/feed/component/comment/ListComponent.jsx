import React, { useState } from "react";
import ReplyListComponent from "./ReplyListComponent";

const ListComponent = ({ commentList }) => {
  console.log(commentList);

  const [replyContent, setReplyContent] = useState("");
  const [activeReplyComment, setActiveReplyComment] = useState(null);
  const handleReplyClick = (commentId) => {
    // 답글 입력을 토글하는 역할
    setActiveReplyComment((prev) => (prev === commentId ? null : commentId));
  };

  const handleReplyChange = (event) => {
    setReplyContent(event.target.value);
  };

  const handleSubmitReply = (commentId) => {
    // 실제로 서버로 답글을 전송하는 로직
    console.log(`답글 등록 - 댓글 ID: ${commentId}, 내용: ${replyContent}`);
    // 답글 등록 후, 상태 초기화
    setReplyContent("");
    setActiveReplyComment(null);
  };

  return (
    <>
      {(commentList ?? []).length > 0 ? (
        commentList.map((comment) => (
          <div
            key={comment.idx}
            className="flex space-x-3 my-1 pb-2 border-b border-gray-200"
          >
            <img
              src=""
              alt="프로필 이미지"
              className="w-10 h-10 rounded-full object-cover"
            />
            <div className="flex flex-col w-full">
              <div className="flex justify-between">
                <p className="font-semibold text-gray-800">
                  {comment.user.nickname}
                </p>
                <div className="flex text-xs text-gray-500 items-center">
                  <button>수정</button>
                  <span className="mx-2">|</span>
                  <button>삭제</button>
                </div>
              </div>
              <p className="text-sm text-gray-600">{comment.content}</p>
              <div className="mt-1 flex justify-between text-xs text-gray-500">
                {/* 좌측: 답글 달기 버튼 */}
                <button
                  className="text-blue-500"
                  onClick={() => handleReplyClick(comment.idx)} // 실제 로직 추가
                >
                  답글 달기
                </button>

                {/* 우측: 등록일 */}
                <span>{comment.regDateStr}</span>
              </div>

              {/* 답글 입력 영역 */}
              {activeReplyComment === comment.idx && (
                <div className="mt-2 flex space-x-2">
                  <input
                    type="text"
                    className="w-full px-3 py-1 text-sm border border-gray-300 rounded-md"
                    placeholder="답글을 입력하세요..."
                    value={replyContent}
                    onChange={handleReplyChange}
                  />
                  <button
                    className="bg-blue-500 text-white px-4 py-1 rounded-md text-sm"
                    onClick={() => handleSubmitReply(comment.idx)}
                  >
                    등록
                  </button>
                </div>
              )}
            </div>
            {/* <ReplyListComponent/> */}
          </div>
        ))
      ) : (
        <div className="flex space-x-3 my-1">댓글이 없습니다</div>
      )}
    </>
  );
};

export default ListComponent;
