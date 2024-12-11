import React, { useEffect, useState } from "react";
import { getCustomList } from "../api/CustomApi";
import Slider from "react-slick";
import user from "../img/user.png";
import { Link } from "react-router-dom";
import styled from "styled-components";

function CustomPrevArrow(props) {
  const { className, onClick } = props;
  return (
    <div className={`${className} custom-prev-arrow`} onClick={onClick}>
      ◀
    </div>
  );
}

function CustomNextArrow(props) {
  const { className, onClick } = props;
  return (
    <div className={`${className} custom-next-arrow`} onClick={onClick}>
      ▶
    </div>
  );
}

export const SliderContainer = styled.div`
  margin: 0px 200px;
  border: solid 1px black;

  .slick-prev:before,
  .slick-next:before {
    display: none !important;
  }

  .custom-prev-arrow,
  .custom-next-arrow {
    width: 40px;
    height: 40px;
    background-color: #3498db;
    border-radius: 50%;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    cursor: pointer;
  }

  .custom-prev-arrow {
    left: -50px !important;
  }

  .custom-next-arrow {
    right: -50px !important;
  }
`;

const ListComponent = () => {
  const [customs, setCustoms] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getCustomList();
        setCustoms(data);
      } catch (error) {
        console.error("Failed to fetch custom list:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen text-lg">
        Loading...
      </div>
    );
  }
  const settings = {
    dots: true,
    infinite: false,
    speed: 500,
    slidesToShow: 2,
    slidesToScroll: 1,
    nextArrow: <CustomNextArrow />,
    prevArrow: <CustomPrevArrow />,
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold text-center mb-8">
        유저들이 만든 코스에요
      </h1>
      {customs.length > 0 ? (
        <ul className="space-y-8">
          {customs.map((custom) => (
            <div
              key={custom.cno}
              className="bg-white shadow-md rounded-lg p-6 border border-gray-200"
            >
              <h2 className="text-xl font-semibold text-gray-800 mb-4">
                {custom.title}
              </h2>
              <p className="text-gray-600 mb-4">{custom.description}</p>
              <h3 className="text-lg font-medium text-gray-700 mb-4">
                코스 목록
              </h3>
              <Link to={`/custom/read/${custom.cno}`}>코스 자세히 보기</Link>
              <SliderContainer>
                <Slider {...settings}>
                  {custom.places.map((place, index) => (
                    <div
                      key={index}
                      className="p-4 bg-gray-100 rounded-lg shadow-sm flex flex-col items-center"
                    >
                      <div
                        className="w-32 h-32 bg-cover bg-center rounded-full mb-4"
                        style={{ backgroundImage: `url(${user})` }}
                      ></div>
                      <p className="text-sm text-gray-800 font-medium">
                        {place.name}
                      </p>
                    </div>
                  ))}
                </Slider>
              </SliderContainer>
            </div>
          ))}
        </ul>
      ) : (
        <p className="text-center text-gray-500">
          등록된 데이트 코스가 없습니다.
        </p>
      )}
    </div>
  );
};

export default ListComponent;
