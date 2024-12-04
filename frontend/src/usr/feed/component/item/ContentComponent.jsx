import React from "react";

const ContentComponent = ({ feed, handleSelectedFeed, feedIdx }) => {
  const { content, feedHashtags } = feed;
  return (
    <>
      {/* 컨텐츠 내용 */}
      <div
        className={`flex flex-col justify-start pl-3 
      space-y-2 w-full break-words whitespace-normal
      ${handleSelectedFeed ? "cursor-pointer" : ""}`}
        onClick={() => handleSelectedFeed && handleSelectedFeed(feedIdx)}
      >
        {content}

        <div className="flex space-x-2">
          {/* 해시태그 */}
          {(feedHashtags ?? []).length > 0 &&
            feedHashtags.map((hashtag, index) => (
              <span className="text-blue-500 font-bold">#{hashtag}</span>
            ))}
        </div>
      </div>
    </>
  );
};

export default ContentComponent;
