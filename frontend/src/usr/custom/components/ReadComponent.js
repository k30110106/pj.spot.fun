import React, { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getCustomDetail, deleteCustom } from "../api/CustomApi";
import Button from "react-bootstrap/Button";
import user from "../img/user.png";
import vector from "../img/Vector.png";

const ReadComponent = () => {
  const mapElement = useRef(null);
  const { cno } = useParams(); // URL 파라미터에서 cno 가져오기
  const navigate = useNavigate(); // 페이지 이동을 위한 navigate
  const [custom, setCustom] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        const data = await getCustomDetail(cno);
        setCustom(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchDetail();
  }, [cno]);

  useEffect(() => {
    if (!mapElement.current || !custom || custom.places.length === 0) return;

    const script = document.createElement("script");
    script.src =
      "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=6v5g0a8cnc";
    script.async = true;

    script.onload = () => {
      const { naver } = window;
      if (!naver) {
        console.error("Naver Map is not loaded.");
        return;
      }

      const map = new naver.maps.Map(mapElement.current, {
        center: new naver.maps.LatLng(
          custom.places[0].latitude,
          custom.places[0].longitude
        ),
        zoom: 14,
      });

      const pathCoords = custom.places.map(
        (place) => new naver.maps.LatLng(place.latitude, place.longitude)
      );

      for (let i = 0; i < pathCoords.length; i++) {
        const position = pathCoords[i];

        new naver.maps.Marker({
          position: position,
          map: map,
          icon: {
            content: `
               <div style="
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        background-color: #00E5E5; /* 마커 배경 색상 */
        border-radius: 50% 50% 50% 0; /* 아래쪽이 뾰족한 핀 형태 */
        transform: rotate(-45deg); /* 회전하여 핀 모양 구현 */
        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.3); /* 그림자 추가 */
      ">
        <div style="
          transform: rotate(45deg); /* 내부 컨텐츠 회전 복구 */
          font-size: 20px;
          font-weight: bold;
          color: white;
        ">
          ${i + 1} <!-- 마커 번호 -->
        </div>
      </div>
            `,
            anchor: new naver.maps.Point(20, 50), // 마커 위치 조정
          },
        });

        if (i > 0) {
          new naver.maps.Polyline({
            map: map,
            path: [pathCoords[i - 1], position],
            strokeColor: "#25E2B6", // 선 색상
            strokeWeight: 3, // 선 두께
            strokeOpacity: 1, // 선 투명도
            strokeStyle: "dashed",
          });
        }
      }
    };

    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, [custom]);

  const handleDelete = async () => {
    if (window.confirm("정말로 삭제하시겠습니까?")) {
      try {
        await deleteCustom(cno);
        alert("정상적으로 삭제되었습니다!");
        navigate("/custom/list"); // 삭제 후 목록 페이지로 이동
      } catch (error) {
        console.error("Error deleting date course:", error);
        alert("Failed to delete the date course. Please try again.");
      }
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!custom) return <div>No detail found.</div>;

  return (
    <div>
      <div
        ref={mapElement}
        style={{
          width: "1135px",
          height: "440px",
          border: "0",
          borderRadius: "25px",
          marginBottom: "20px",
        }}
      />
      <h1 className="text-2xl font-bold mb-2">{custom.title}</h1>
      <div className="flex space-x-2 mb-4">
        {custom.tags.map((tag) => (
          <span class="px-4 py-1 text-sm font-semibold text-custom-cyan border border-custom-cyan rounded-full">
            {tag}
          </span>
        ))}
      </div>
      <div className="container mx-auto px-4">
        {/* 카드 그리드 */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {custom.places.map((place, index) => (
            <div
              key={index}
              className="bg-white shadow-lg rounded-lg overflow-hidden relative"
            >
              {/* 이미지 */}
              <div className="relative">
                <img
                  src={user}
                  alt={place.name}
                  className="w-full h-40 object-cover"
                />

                {/* 정보 텍스트와 버튼 */}
                <div className="absolute bottom-0 left-0 right-0 flex justify-between items-center px-4 py-2 text-white">
                  {/* 번호와 이름 */}
                  <div>
                    <p className="text-lg font-bold">0{index + 1}</p>
                    <p className="text-base">{place.name}</p>
                  </div>

                  {/* 길찾기 버튼 */}
                  <button
                    onClick={() =>
                      window.open(
                        `https://map.kakao.com/link/to/${encodeURIComponent(
                          place.name
                        )},${place.latitude},${place.longitude}`
                      )
                    }
                    className="flex flex-col items-center justify-center bg-blue-500 hover:bg-blue-700 text-white font-bold w-12 h-12 rounded-full"
                  >
                    <img src={vector} alt="길찾기" className="w-4 h-4 mb-1" />
                    <span className="text-[10px] font-bold">길 찾기</span>
                  </button>
                </div>
              </div>

              {/* 예상 비용 및 소요 시간 */}
              <div className="p-4">
                <div className="flex justify-between items-center text-sm">
                  <p className="text-gray-600">1인 예상 비용</p>
                  <p className="font-bold">{place.estimatedCost}원</p>
                </div>
                <div className="flex justify-between items-center text-sm">
                  <p className="text-gray-600">소요 시간</p>
                  <p className="font-bold">{place.durationMinutes}분</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div style={{ marginTop: "20px" }}>
        <Button
          variant="warning"
          onClick={() => navigate(`/custom/update/${cno}`)}
          style={{ marginRight: "10px" }}
        >
          수정
        </Button>
        <Button variant="danger" onClick={handleDelete}>
          삭제
        </Button>
      </div>
    </div>
  );
};

export default ReadComponent;
