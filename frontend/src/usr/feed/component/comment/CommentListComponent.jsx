import { useState } from "react";
import ReplyListComponent from "./ReplyListComponent";
import InputComponent from "./InputComponent";
import CommentProfileComponent from "./CommentProfileComponent";
import { BiMessageAltDots } from "react-icons/bi";

const ListComponent = ({
  commentList,
  handleReplyEvent,
  handleCommentModifyEvent,
  handleCommentDeleteEvent,
  handleReplyModifyEvent,
  handleReplyDeleteEvent,
}) => {
  const [activeReplyComment, setActiveReplyComment] = useState(null);
  const handleReplyClick = (commentId) => {
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
            {/* 프로필 */}
            <CommentProfileComponent
              comment={comment}
              handleReplyClick={handleReplyClick}
              handleCommentModifyEvent={handleCommentModifyEvent}
              handleCommentDeleteEvent={handleCommentDeleteEvent}
            />

            {/* 답글 입력 영역 */}
            {activeReplyComment === comment.idx && (
              <InputComponent handleCommentEvent={handleCommentEvent} />
            )}

            {(comment.replyList ?? []).length > 0 && (
              <ReplyListComponent
                parentIdx={comment.idx}
                replyList={comment.replyList}
                handleReplyModifyEvent={handleReplyModifyEvent}
                handleReplyDeleteEvent={handleReplyDeleteEvent}
              />
            )}
          </div>
        ))
      ) : (
        <div className="w-full h-full flex flex-col justify-center items-center text-gray-600 space-y-4">
          <BiMessageAltDots className="text-4xl" />
          <span className="font-semibold text-xl">댓글이 없습니다</span>
        </div>
      )}
    </>
  );
};

export default ListComponent;
