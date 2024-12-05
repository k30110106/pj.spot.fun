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

  const handleInputEvent = (e) => {
    if (e.key === "Enter") {
      submitComment();
    }
  };

  return (
    <div className="mt-2 flex space-x-3 w-full">
      <input
        type="text"
        className="w-4/5 px-3 py-1 text-sm border border-gray-300 rounded-md"
        placeholder="댓글을 입력하세요..(최대 50자)"
        maxLength="50"
        value={commentText}
        onChange={(e) => setCommentText(e.target.value)}
        onKeyDown={handleInputEvent}
      />
      <button
        className="w-1/5 bg-blue-500 text-white px-2 py-1 rounded-md text-sm"
        onClick={submitComment}
      >
        등록
      </button>
    </div>
  );
};

export default InputComponent;
