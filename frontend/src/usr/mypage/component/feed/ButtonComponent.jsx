import { FaPlus } from "react-icons/fa";

const ButtonComponent = ({
  userIdx,
  loginUserIdx,
  feedIdx,
  openDetailModal,
  openModifyModal,
  handleListDeleteEvent,
}) => {
  const isMine = parseInt(userIdx) === parseInt(loginUserIdx);

  return (
    <>
      {isMine ? (
        <div
          className="absolute inset-0 flex justify-center items-center opacity-0 
              group-hover:opacity-100 transition-opacity duration-300 bg-gray-500 bg-opacity-30 z-10"
        >
          <div className="text-white flex flex-col items-center space-y-3">
            {/* 상세, 수정, 삭제 버튼들 */}
            <button
              className="w-full px-4 py-2 bg-indigo-500 text-white rounded-lg text-sm hover:bg-indigo-600 transition-colors duration-300 focus:outline-none cursor-pointer"
              onClick={(e) => {
                e.stopPropagation(); // 이벤트 버블링 방지
                openDetailModal(`${feedIdx}`);
              }}
            >
              상세
            </button>
            <button
              className="w-full px-4 py-2 bg-blue-500 text-white rounded-lg text-sm hover:bg-blue-600 transition-colors duration-300 focus:outline-none"
              onClick={(e) => {
                e.stopPropagation(); // 이벤트 버블링 방지
                openModifyModal(`${feedIdx}`);
              }}
            >
              수정
            </button>
            <button
              className="w-full px-4 py-2 bg-red-500 text-white rounded-lg text-sm hover:bg-red-600 transition-colors duration-300 focus:outline-none"
              onClick={(e) => {
                e.stopPropagation(); // 이벤트 버블링 방지
                handleListDeleteEvent(`${feedIdx}`);
              }}
            >
              삭제
            </button>
          </div>
        </div>
      ) : (
        <div
          className="absolute inset-0 flex justify-center items-center opacity-0 
            group-hover:opacity-100 transition-opacity duration-300 bg-gray-500 bg-opacity-30 z-10 
            cursor-pointer"
          onClick={() => openDetailModal(`${feedIdx}`)}
        >
          <div className="text-white flex items-center">
            <FaPlus size="1.2rem" />
          </div>
        </div>
      )}
    </>
  );
};

export default ButtonComponent;
