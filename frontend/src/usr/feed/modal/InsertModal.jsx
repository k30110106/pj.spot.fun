import React, { useRef, useState } from "react";
import HashTagModal from "./HashTagModal";
import { postFeedInsertApi } from "../api/FeedApi";
import ImageComponent from "../component/insert/ImageComponent";
import ProfileComponent from "../component/item/ProfileComponent";
import { useBasic } from "../../../common/context/BasicContext";
import ContentComponent from "../component/insert/ContentComponent";

const InsertModal = ({ closeInsertModal }) => {
  // 전역 값
  const { userInfo } = useBasic();

  // 이미지
  const useFileRef = useRef([]);

  // 콘텐츠
  const useTextRef = useRef(null);

  // 해시태그
  const [selectedHashtags, setSelectedHashtags] = useState([]);
  const [isHashtagModalOpen, setIsHashtagModalOpen] = useState(false);
  const closeHashtagModal = () => {
    setIsHashtagModalOpen(false);
  };

  // 해시태그 추가
  const addHashtag = (hashtag) => {
    setSelectedHashtags((prevHashtags) => [...prevHashtags, hashtag]);
  };

  // 글 등록
  const handleFeedSubmit = () => {
    const content = useTextRef.current.value;
    if (content.trim().length < 1) {
      console.log("내용을 입력해주세요..");
      return false;
    }

    const form = new FormData();

    useFileRef.current.forEach((item) => {
      const itemFile = item.files[0];
      if (itemFile) {
        form.append("uploadFiles", itemFile);
      }
    });
    form.append("content", content);

    postFeedInsertApi(form)
      .then((res) => {
        if (res) {
          closeInsertModal(res);
          return;
        }
        console.log("등록 실패 에러코드 .." + res.status);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg w-3/4 max-w-4xl p-6 relative">
        {/* 상단 툴바 X 버튼 */}
        <div className="flex justify-end items-center mb-4">
          <button
            onClick={() => closeInsertModal(null)}
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
        <ProfileComponent user={{ ...userInfo, idx: userInfo.userIdx }} />

        {/* 중간영역: 글 내용 및 해시태그 */}
        <div className="mt-4">
          {/* 컨텐츠 */}
          <ContentComponent useTextRef={useTextRef} />

          {/* 해시태그 */}
          <div className="mt-2 flex items-center space-x-2">
            <button
              className="text-blue-500"
              onClick={() => setIsHashtagModalOpen(true)}
            >
              해시태그 추가
            </button>
            <div className="flex flex-wrap space-x-2">
              {selectedHashtags.map((hashtag, index) => (
                <span key={index} className="text-sm text-blue-500">
                  {" "}
                  {hashtag}{" "}
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* 하단영역: 이미지 업로드 */}
        <div className="mt-4">
          <div className="flex justify-center">
            <ImageComponent
              id={"upload1"}
              useFileRef={(e) => (useFileRef.current[0] = e)}
            />
            <ImageComponent
              id={"upload2"}
              useFileRef={(e) => (useFileRef.current[1] = e)}
            />
            <ImageComponent
              id={"upload3"}
              useFileRef={(e) => (useFileRef.current[2] = e)}
            />
          </div>
        </div>

        {/* 등록 버튼 */}
        <div className="mt-4 flex justify-end">
          <button
            onClick={handleFeedSubmit}
            className="bg-blue-500 text-white py-2 px-6 rounded-md"
          >
            등록
          </button>
        </div>

        {/* 해시태그 모달 */}
        {isHashtagModalOpen && (
          <HashTagModal
            closeHashtagModal={closeHashtagModal}
            addHashtag={addHashtag}
          />
        )}
      </div>
    </div>
  );
};

export default InsertModal;
