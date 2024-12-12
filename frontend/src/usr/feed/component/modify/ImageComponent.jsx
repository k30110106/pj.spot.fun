import { useEffect, useState } from "react";
import { API_BASE_URL } from "../../api/FeedApi";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ImageComponent = ({
  id,
  useFileRef,
  img,
  handleDeleteOriginImageIdx,
}) => {
  // 이미지 파일 선택
  //console.log(img);
  const [imageSrc, setImageSrc] = useState(initSrc);
  const handleImageChange = (e) => {
    const file = e.target.files[0];

    if (file && file.type.startsWith("image")) {
      setImageSrc(URL.createObjectURL(file));
      setShowDeleteButton(true);

      if (img?.idx) {
        handleDeleteOriginImageIdx(img.idx);
      }
    } else {
      setImageSrc(initSrc);
      e.target.value = "";
      setShowDeleteButton(false);
    }
  };

  // 이미지 파일 삭제버튼
  const [showDeleteButton, setShowDeleteButton] = useState(false); // X 버튼 표시 여부
  const handleDeleteImage = () => {
    setImageSrc(initSrc); // 이미지 삭제
    setShowDeleteButton(false); // 삭제 버튼 숨기기
    document.querySelector(`#${id}`).value = "";

    if (img?.idx) {
      handleDeleteOriginImageIdx(img.idx);
    }
  };

  useEffect(() => {
    if (img?.imgSrc) {
      setImageSrc(img.imgSrc);
      setShowDeleteButton(true);
    }
  }, [img]);

  return (
    <>
      <div className="w-1/3 h-full relative">
        <img
          src={imageSrc}
          alt="upload"
          className="mt-4 border-solid border-2 border-indigo-400 rounded-md cursor-pointer w-full min-h-48 max-h-48 object-contain"
          onClick={() => document.querySelector(`#${id}`).click()}
        />

        {/* 삭제 버튼: 이미지가 선택되었을 때만 표시 */}
        {showDeleteButton && (
          <button
            onClick={handleDeleteImage}
            className="absolute top-6 right-3 text-white bg-red-500 rounded-full px-2 border border-white"
          >
            X
          </button>
        )}

        <input
          type="file"
          id={id}
          accept="image/*"
          ref={useFileRef}
          onChange={handleImageChange}
          className="hidden"
        />
      </div>
    </>
  );
};

export default ImageComponent;
