import React from "react";
import { MdSubdirectoryArrowRight } from "react-icons/md";

const ReplyListComponent = ({ replyList }) => {
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
              src=""
              alt="프로필 이미지"
              className="w-8 h-8 rounded-full object-cover"
            />
            <div className="flex flex-col w-full">
              <div className="flex justify-between items-center text-xs text-gray-500">
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
