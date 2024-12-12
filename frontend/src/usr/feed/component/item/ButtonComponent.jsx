import { FaRegHeart } from "react-icons/fa";
import { FaHeart } from "react-icons/fa";
import { BiMessageRoundedDots } from "react-icons/bi";
import { useBasic } from "../../../../common/context/BasicContext";

const ButtonComponent = ({ feed, openDetailModal, handleLikesEvent }) => {
  const { userIdx } = useBasic();
  const { idx, commentCount, likeCount, likedYn, regDateStr } = feed;

  // 좋아요 토클 이벤트
  const toggleLikes = () => {
    const newLikedYn = !likedYn;
    const newLikeCount = newLikedYn ? likeCount + 1 : likeCount - 1;
    handleLikesEvent(idx, newLikedYn, newLikeCount);
  };

  return (
    <>
      <div className="flex mt-1 py-2 justify-between">
        {/* 좋아요 + 댓글 */}
        <div className="flex justify-start pl-3 space-x-3 w-2/3">
          <div className="flex items-center">
            <button
              className={`flex items-center rounded-full p-1 justify-center border border-transparent 
                ${
                  userIdx
                    ? "hover:bg-red-100 focus:outline-none transition-all duration-200"
                    : ""
                }`}
              onClick={toggleLikes}
            >
              {likedYn ? (
                <FaHeart size="1.8rem" />
              ) : (
                <FaRegHeart size="1.8rem" />
              )}
            </button>
            <span>{likeCount > 99 ? "99+" : likeCount}</span>
          </div>
          <div className="flex items-center">
            <button
              className={`flex items-center rounded-full p-1 justify-center border border-transparent 
                            ${
                              openDetailModal
                                ? "hover:bg-red-100 focus:outline-none transition-all duration-200"
                                : ""
                            }`}
              onClick={openDetailModal}
            >
              <BiMessageRoundedDots size="1.8rem" />
            </button>
            <span>{commentCount > 99 ? "99+" : commentCount}</span>
          </div>
        </div>

        <div className="flex items-center justify-between pr-3">
          {/* 등록일시 */}
          {regDateStr}
        </div>
      </div>
    </>
  );
};

export default ButtonComponent;
