import { useState } from "react";

const ContentComponent = ({ useTextRef, feed }) => {
  const [feedContent, setFeedContent] = useState(feed.content);

  const handleContentEvent = (e) => {
    setFeedContent(e.target.value);
  };

  return (
    <>
      <textarea
        className="w-full p-3 border border-gray-300 rounded-md"
        placeholder="내용을 입력해주세요..(최대 100자)"
        rows="5"
        maxLength="100"
        ref={useTextRef}
        value={feedContent}
        onChange={handleContentEvent}
      />
    </>
  );
};

export default ContentComponent;
