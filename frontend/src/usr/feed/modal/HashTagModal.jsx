import { useEffect, useState } from "react";
import { getHashtagApi } from "../api/FeedApi";

const HashTagModal = ({
  closeHashtagModal,
  hashtagList,
  handleSelectHashTagEvent,
}) => {
  // 해시태그 목록
  const [list, setList] = useState([]);
  useEffect(() => {
    getHashtagApi()
      .then((data) => {
        setList(data);
      })
      .catch((err) => {
        console.log("[해시태그 목록] 조회를 실패했습니다");
        console.log(err);
      });
  }, []);

  return (
    <div
      className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center z-50"
      onClick={closeHashtagModal}
    >
      <div
        className="bg-white rounded-lg w-80 p-6 flex flex-col"
        onClick={(e) => e.stopPropagation()}
      >
        {/* 상단 툴바 X 버튼 */}
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">해시태그 선택</h3>
          <button
            onClick={closeHashtagModal}
            className="text-gray-500 hover:text-gray-800"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              className="w-6 h-6"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        {/* 해시태그 리스트 */}
        <div className="mt-4 flex flex-wrap gap-3 flex-grow">
          {list.length > 0 &&
            list.map((tag) => (
              <button
                key={tag.idx}
                onClick={() => handleSelectHashTagEvent(tag)}
                className={`${
                  hashtagList.find((item) => item.hashtagIdx === tag.idx)
                    ? "bg-blue-500 text-white"
                    : "bg-gray-200 text-gray-800"
                } px-4 py-2 rounded-full hover:bg-blue-500 hover:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out`}
              >
                {tag.tagName}
              </button>
            ))}
        </div>
      </div>
    </div>
  );
};

export default HashTagModal;
