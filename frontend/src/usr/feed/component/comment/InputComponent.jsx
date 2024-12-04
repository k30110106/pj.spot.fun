import { useState } from "react";

const InputComponent = ({ handleCommentEvent }) => {
  const [commentText, setCommentText] = useState("");
  const submitComment = () => {
    if (commentText.trim().length < 1) {
      console.log("댓글을 작성해주세요");
      return;
    }
    handleCommentEvent(commentText);
    setCommentText("");
  };

  const enterEvent = (e) => {
    if (e.key === "Enter") {
      submitComment();
    }
  };

  return (
    <>
      <input
        type="text"
        className="grow"
        placeholder="댓글을 입력해주세요..(최대 50자)"
        maxLength="50"
        value={commentText}
        onChange={(e) => setCommentText(e.target.value)}
        onKeyDown={enterEvent}
      />
      <button
        type="button"
        className="bg-blue-500 text-white py-2 px-6 w-1/5"
        onClick={submitComment}
      >
        등록
      </button>
    </>
  );
};

export default InputComponent;
