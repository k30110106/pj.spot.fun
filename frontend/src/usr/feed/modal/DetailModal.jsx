import { useEffect, useState, useCallback } from "react";
import { getFeedCommentListApi, postCommentApi } from "../api/FeedApi";
import ImageComponent from "../component/item/ImageComponent";
import ProfileComponent from "../component/item/ProfileComponent";
import ContentComponent from "../component/item/ContentComponent";
import ButtonComponent from "../component/item/ButtonComponent";
import ListComponent from "../component/comment/ListComponent";
import InputComponent from "../component/comment/InputComponent";
import { useBasic } from "../../../common/context/BasicContext";

const DetailModal = ({
  feed,
  closeDetailModal,
  handleLikesEvent,
  handleCommentCountEvent,
}) => {
  const { userInfo } = useBasic();
  //console.log(userInfo);

  // 댓글목록 조회
  const [commentList, setCommentList] = useState([]);
  useEffect(() => {
    if (feed) {
      getFeedCommentListApi(feed.idx)
        .then((data) => {
          setCommentList(data);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, []);

  // 댓글 등록
  const handleCommentEvent = useCallback(
    (targetContent) => {
      const param = {
        idx: feed.idx,
        content: targetContent,
      };

      postCommentApi(param)
        .then((data) => {
          if (data) {
            setCommentList((prevCommentList) => [...prevCommentList, data]);
            handleCommentCountEvent(feed.idx, "new");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    [feed.idx]
  );

  return (
    <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg w-9/12 h-5/6 p-6 flex">
        <div className="w-3/5 relative">
          {/* 왼쪽 영역 */}
          {/* 이미지 */}
          {(feed.feedImages ?? []).length > 0 && (
            <ImageComponent feedImages={feed.feedImages} />
          )}
        </div>

        <div className="w-2/5 pl-3 h-full flex flex-col justify-center">
          {/* 오른쪽 영역 */}
          <div className="flex justify-between mb-4 items-start">
            {/* 프로필 */}
            {feed.user && <ProfileComponent user={feed.user} />}

            {/* 상단 툴바 X 버튼 */}
            <button
              onClick={closeDetailModal}
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

          {/* 컨텐츠 + 해시태그 */}
          <ContentComponent feed={feed} />

          {/* 버튼 + 등록일시 */}
          <ButtonComponent feed={feed} handleLikesEvent={handleLikesEvent} />

          <div className="border border-gray-200 overflow-y-auto p-4 space-y-4 h-full">
            {/* 댓글 목록 + 등록인풋 */}
            <ListComponent commentList={commentList} />
          </div>

          <div className="flex space-x-3 border border-gray-200 justify-end w-full">
            {/*댓글 입력 + 등록*/}
            {userInfo != null && (
              <InputComponent handleCommentEvent={handleCommentEvent} />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DetailModal;
