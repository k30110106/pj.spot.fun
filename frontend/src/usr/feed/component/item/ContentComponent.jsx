const ContentComponent = ({ feed, openDetailModal }) => {
  const { content, feedHashtags } = feed;
  return (
    <>
      {/* 컨텐츠 내용 */}
      <div
        className={`flex flex-col justify-start px-3 
      space-y-2 w-full break-words whitespace-normal
      ${openDetailModal ? "cursor-pointer" : ""}`}
        onClick={openDetailModal}
      >
        {content}

        <div className="flex space-x-2">
          {/* 해시태그 */}
          {feedHashtags != null &&
            (feedHashtags ?? []).length > 0 &&
            feedHashtags.map((tag, index) => (
              <span key={tag.idx} className="text-blue-500 font-bold">
                #{tag.tagName}
              </span>
            ))}
        </div>
      </div>
    </>
  );
};

export default ContentComponent;
