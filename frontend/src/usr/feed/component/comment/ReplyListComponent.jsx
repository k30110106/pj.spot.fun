import React from "react";

const ReplyListComponent = ({ comment }) => {
  return (
    <>
      {/* 대댓글 (reply) */}
      {comment.replies && comment.replies.length > 0 && (
        <div className="pl-10 mt-2">
          {comment.replies.map((reply) => (
            <div
              key={reply.idx}
              className="flex space-x-3 my-1 pb-2 pl-10 border-t border-gray-300"
            >
              <img
                src={reply.user.profileImageUrl || ""}
                alt="프로필 이미지"
                className="w-8 h-8 rounded-full object-cover"
              />
              <div className="flex flex-col w-full">
                <div className="flex justify-between text-xs text-gray-500">
                  <p className="font-semibold text-gray-800">
                    {reply.user.nickname}
                  </p>
                  <div className="flex items-center">
                    <button className="text-blue-500">수정</button>
                    <span className="mx-2">|</span>
                    <button className="text-red-500">삭제</button>
                  </div>
                </div>
                <p className="text-sm text-gray-600 mt-1">{reply.content}</p>
                <div className="mt-1 flex justify-between text-xs text-gray-500">
                  {/* 좌측: 답글 달기 버튼 */}
                  <button
                    className="text-blue-500"
                    onClick={() =>
                      console.log(`대댓글 달기 - 대댓글 ID: ${reply.idx}`)
                    }
                  >
                    답글 달기
                  </button>

                  {/* 우측: 등록일 */}
                  <span>{reply.regDateStr}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );
};

export default ReplyListComponent;
