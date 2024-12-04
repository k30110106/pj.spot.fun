import React, { useState } from "react";
import { API_BASE_URL } from "../../api/FeedApi";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ImageComponent = ({ id, useFileRef, handleImageUrlEvent }) => {
  // 이미지 파일 선택
  const [imageSrc, setImageSrc] = useState(initSrc);
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file.type.startsWith("image")) {
      const getUrl = file ? URL.createObjectURL(file) : initSrc;
      setImageSrc(getUrl);
    }
  };

  return (
    <>
      <div className="w-1/3 h-full">
        <img
          src={imageSrc}
          alt="upload"
          className="mt-4 border-solid border-2 border-indigo-400 rounded-md cursor-pointer w-full min-h-48 max-h-48 object-contain"
          onClick={() => document.querySelector(`#${id}`).click()}
        />
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
