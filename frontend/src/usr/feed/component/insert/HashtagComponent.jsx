const HashtagComponent = ({ hashtagList, handleDeleteHashTagEvent }) => {
  return (
    <>
      {hashtagList.map((tag) => (
        <span
          key={tag.hashtagIdx}
          className="inline-flex items-center text-sm font-medium text-blue-700 bg-blue-100 rounded-full px-3 py-1 hover:bg-blue-200 transition duration-200 ease-in-out"
        >
          {tag.tagName}
          <button
            onClick={() => handleDeleteHashTagEvent(tag)} // 선택한 해시태그 삭제 함수
            className="ml-2 text-blue-500 hover:text-blue-700"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              className="w-4 h-4"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </span>
      ))}
    </>
  );
};

export default HashtagComponent;
