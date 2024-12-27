import React, { useState } from "react";
import { Link } from "react-router-dom";

const SliderComponent = ({ items }) => {
  const itemsPerSlide = 5; // 한 슬라이드에 표시할 이미지 수
  const [currentIndex, setCurrentIndex] = useState(0);

  const nextSlide = () => {
    setCurrentIndex((prevIndex) => (prevIndex + itemsPerSlide) % items.length);
  };

  const prevSlide = () => {
    setCurrentIndex(
      (prevIndex) => (prevIndex - itemsPerSlide + items.length) % items.length
    );
  };

  return (
    <>
      <div className="relative group">
        {/* 슬라이드 이미지 컨테이너 */}
        <div className="flex overflow-hidden">
          <div
            className="flex transition-transform duration-300"
            style={{
              transform: `translateX(-${
                (currentIndex / itemsPerSlide) * 100
              }%)`,
            }}
          >
            {items.map((item, index) => (
              <div
                key={index}
                className="w-full sm:w-1/5 flex-shrink-0 p-2 relative"
              >
                {/* 이미지 영역 */}
                <div className="relative">
                  <img
                    src={item.image}
                    alt={item.title}
                    className="w-full h-auto rounded-lg object-contain shrink-0"
                  />
                  {/* 호버 시 나타나는 바로가기 버튼 */}
                  <Link
                    to={`/course/${item.title}`}
                    className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-black p-2 rounded-full opacity-0 hover:opacity-100 transition-opacity duration-300"
                  >
                    바로가기 &gt;
                  </Link>
                </div>
                <>
                  <h3 className="text-center mt-2">{item.title}</h3>
                  <div className="flex justify-center ">
                    <span className="text-start text-xs  text-gray-500">
                      {item.address}
                    </span>
                    {/* 위 수정 해야함 */}
                    {/* <span className="text-end text-sm ">{item.price}{item.time}{item.km}</span> */}
                    <span className=" text-start text-xs text-gray-400">
                      {item.title}
                    </span>
                  </div>
                </>
              </div>
            ))}
          </div>
        </div>

        {items.length > 1 && (
          <>
            <div className="absolute top-1/2 left-0 transform -translate-y-[100%] opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                          <button
                            onClick={prevSlide}
                            className="bg-emerald-400 text-white font-thin px-3 py-2
                  rounded-full hover:bg-emerald-500"
                          >
                            &lt;
                          </button>
                        </div>
                        <div className="absolute top-1/2 right-0 transform -translate-y-[100%] opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                          <button
                            onClick={nextSlide}
                            className="bg-emerald-400 text-white font-thin px-3 py-2
                  rounded-full hover:bg-emerald-500"
                          >
                            &gt;
                          </button>
            </div>
          </>
        )}
      </div>
    </>
  );
};

export default SliderComponent;
