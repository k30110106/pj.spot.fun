import React from "react";

const ListComponent = ({ commentList }) => {
  return (
    <>
      {(commentList ?? []).length > 0 ? (
        commentList.map((comment) => (
          <div key={comment.idx} className="flex space-x-3 my-1">
            <img
              src=""
              alt="프로필 이미지"
              className="w-10 h-10 rounded-full object-cover"
            />
            <div>
              <p className="font-semibold text-gray-800">
                {comment.user.nickname}
              </p>
              <p className="text-sm text-gray-600">{comment.content}</p>
            </div>
          </div>
        ))
      ) : (
        <div className="flex space-x-3 my-1">댓글이 없습니다</div>
      )}
    </>
  );
};

export default ListComponent;
