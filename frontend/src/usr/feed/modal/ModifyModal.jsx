import { useEffect, useRef, useState } from "react";
import { useBasic } from "../../../common/context/BasicContext";
import HashTagModal from "./HashTagModal";
import ProfileComponent from "../component/item/ProfileComponent";
import ContentComponent from "../component/modify/ContentComponent";
import HashtagComponent from "../component/modify/HashtagComponent";
import ImageComponent from "../component/modify/ImageComponent";
import { API_BASE_URL, putFeedModifyApi } from "../api/FeedApi";

const ModifyModal = ({ feed, closeModifyModal }) => {
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  // 이미지
  const useFileRef = useRef([]);
  const [imgList, setImgList] = useState([]);
  const [delImgList, setDelImgList] = useState([]);
  const handleDeleteOriginImageIdx = (idx) => {
    setDelImgList((prevList) => {
      if (prevList.find((item) => item === idx)) {
        return [...prevList];
      }
      return [...prevList, idx];
    });
  };

  // 콘텐츠
  const useTextRef = useRef(null);

  // 해시태그 모달
  const [isHashtagModalOpen, setIsHashtagModalOpen] = useState(false);
  const closeHashtagModal = () => {
    setIsHashtagModalOpen(false);
  };

  // 해시태그 이벤트
  const [hashtagList, setHashtagList] = useState([]);
  const handleSelectHashTagEvent = (tag) => {
    const tagObj = {
      hashtagIdx: tag.idx,
      tagName: tag.tagName,
    };
    setHashtagList((prevList) => {
      if (prevList.find((item) => item.hashtagIdx === tagObj.hashtagIdx)) {
        return prevList.filter((item) => item.hashtagIdx !== tagObj.hashtagIdx);
      } else {
        return [...prevList, tagObj];
      }
    });
  };

  // 해시태그 삭제
  const handleDeleteHashTagEvent = (tag) => {
    setHashtagList((prevList) => {
      return prevList.filter((item) => item.hashtagIdx !== tag.hashtagIdx);
    });
  };

  // 수정 등록
  const handleFeedSubmit = () => {
    const content = useTextRef.current.value;
    if (content.trim().length < 1) {
      console.log("[임시] 내용을 입력해주세요");
      return false;
    }

    const form = new FormData();

    useFileRef.current.forEach((item) => {
      const itemFile = item.files[0];
      if (itemFile) {
        form.append("uploadFiles", itemFile);
      }
    });
    delImgList.forEach((item) => {
      if (item) {
        form.append("deleteFiles", item);
      }
    });
    form.append("content", content);

    hashtagList.map((item, index) =>
      form.append(
        `feedHashtags[${index}].hashtagIdx`,
        parseInt(item.hashtagIdx)
      )
    );

    form.append("idx", feed.idx);

    putFeedModifyApi(form)
      .then((data) => {
        if (data) {
          closeModifyModal(data);
          return;
        }
      })
      .catch((err) => {
        console.log("[피드수정] 등록을 실패했습니다");
        console.log(err);
      });
  };

  useEffect(() => {
    const getImgList = feed.feedImages.map((item) => {
      return {
        ...item,
        imgSrc: `${API_BASE_URL}/api/usr/feed/image/${item.uploadName}`,
      };
    });
    setImgList([...getImgList]);
    setHashtagList([...feed.feedHashtags]);
  }, []);

  return (
    <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg w-3/4 max-w-4xl p-6 relative">
        {/* 상단 툴바 X 버튼 */}
        <div className="flex justify-end items-center mb-4">
          <button
            onClick={() => closeModifyModal(null)}
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

        {/* 상단영역: 프로필 정보 */}
        <ProfileComponent feedUserInfo={feed.user} pageType={"modify"} />

        {/* 중간영역: 글 내용 및 해시태그 */}
        <div className="mt-4">
          {/* 컨텐츠 */}
          <ContentComponent useTextRef={useTextRef} feed={feed} />

          {/* 해시태그 */}
          <div className="mt-2 flex items-center space-x-4">
            <button
              className="text-blue-500 border-2 border-blue-500 rounded-full py-2 px-6 font-semibold text-sm hover:bg-blue-500 hover:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out"
              onClick={() => setIsHashtagModalOpen(true)}
            >
              해시태그 추가
            </button>

            {/* 선택된 해시태그들 */}
            <div className="flex flex-wrap gap-2">
              {(hashtagList ?? []).length > 0 && (
                <HashtagComponent
                  hashtagList={hashtagList}
                  handleDeleteHashTagEvent={handleDeleteHashTagEvent}
                />
              )}
            </div>
          </div>
        </div>

        {/* 하단영역: 이미지 업로드 */}
        <div className="mt-4">
          <div className="flex justify-center">
            <ImageComponent
              id={"upload1"}
              useFileRef={(e) => (useFileRef.current[0] = e)}
              img={imgList[0]}
              handleDeleteOriginImageIdx={handleDeleteOriginImageIdx}
            />
            <ImageComponent
              id={"upload2"}
              useFileRef={(e) => (useFileRef.current[1] = e)}
              img={imgList[1]}
              handleDeleteOriginImageIdx={handleDeleteOriginImageIdx}
            />
            <ImageComponent
              id={"upload3"}
              useFileRef={(e) => (useFileRef.current[2] = e)}
              img={imgList[2]}
              handleDeleteOriginImageIdx={handleDeleteOriginImageIdx}
            />
          </div>
        </div>

        {/* 등록 버튼 */}
        <div className="mt-4 flex justify-end">
          <button
            onClick={handleFeedSubmit}
            className="bg-blue-500 text-white py-2 px-6 rounded-md"
          >
            수정
          </button>
        </div>

        {/* 해시태그 모달 */}
        {isHashtagModalOpen && (
          <HashTagModal
            closeHashtagModal={closeHashtagModal}
            hashtagList={hashtagList}
            handleSelectHashTagEvent={handleSelectHashTagEvent}
          />
        )}
      </div>
    </div>
  );
};

export default ModifyModal;
