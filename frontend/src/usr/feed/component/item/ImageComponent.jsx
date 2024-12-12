import { useState } from "react";
import { API_BASE_URL } from "../../api/FeedApi";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ImageComponent = ({ openDetailModal, feedImages }) => {
  const [currentIndex, setCurrentIndex] = useState(0);

  // 이미지 슬라이드
  const prevImage = () => {
    setCurrentIndex(
      (prevIndex) => (prevIndex - 1 + feedImages.length) % feedImages.length
    );
  };

  const nextImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % feedImages.length);
  };

  return (
    <>
      <div className="w-full h-full mx-auto relative overflow-hidden">
        {/* 이미지 슬라이드 */}
        <div
          className={`w-full h-full flex transition-transform duration-500 
                    ${openDetailModal ? "cursor-pointer" : ""}`}
          style={{
            transform: `translateX(-${currentIndex * 100}%)`, // 인덱스에 맞게 슬라이드 이동
          }}
          onClick={openDetailModal}
        >
          {feedImages.length > 0 ? (
            feedImages.map((image) => (
              <img
                key={image.idx}
                src={`${API_BASE_URL}/api/usr/feed/image/${image.uploadName}`}
                alt={image.originName}
                className="w-full h-full object-contain shrink-0"
              />
            ))
          ) : (
            <img
              src={initSrc}
              alt="업로드 이미지"
              className="w-full h-full object-contain shrink-0"
            />
          )}
        </div>
      </div>

      {/* 슬라이드 버튼 */}
      {feedImages.length > 1 && (
        <>
          <div className="absolute top-1/2 left-5 transform -translate-y-1/2">
            <button
              onClick={prevImage}
              className="bg-gray-800 text-white p-2 rounded-full hover:bg-gray-600"
            >
              &lt;
            </button>
          </div>
          <div className="absolute top-1/2 right-5 transform -translate-y-1/2">
            <button
              onClick={nextImage}
              className="bg-gray-800 text-white p-2 rounded-full hover:bg-gray-600"
            >
              &gt;
            </button>
          </div>
        </>
      )}
    </>
  );
};

export default ImageComponent;
