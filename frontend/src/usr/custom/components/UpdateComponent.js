import React, { useEffect, useState } from "react";
import { searchPlaces, updateCustom, getCustomDetail } from "../api/CustomApi";
import { useParams, useNavigate } from "react-router-dom";
import Card from "react-bootstrap/Card";
import user from "../img/user.png";

const WITH_TAGS = [
  "연인과",
  "가족과",
  "친구와",
  "나홀로",
  "반려동물과",
  "아이와",
  "부모님과",
];

const DO_TAGS = [
  "놀러가기",
  "데이트",
  "맛집탐방",
  "소개팅",
  "기념일",
  "핫플탐방",
  "힐링",
  "쇼핑",
  "여행",
  "액티비티",
  "공연/전시",
];

const UpdateComponent = () => {
  const { cno } = useParams(); // URL에서 cno를 가져옴
  const navigate = useNavigate(); // 수정 후 페이지 이동
  const [currentStep, setCurrentStep] = useState(1);
  const [address, setAddress] = useState(""); // 검색할 주소
  const [places, setPlaces] = useState([]); // 검색 결과
  const [selectedPlaces, setSelectedPlaces] = useState([]); // 선택한 장소
  const [title, setTitle] = useState(""); // 데이트 코스 제목
  const [description, setDescription] = useState(""); // 데이트 코스 설명
  const [selectedTags, setSelectedTags] = useState([]); // 태그
  const [loading, setLoading] = useState(true); // 로딩 상태

  // 수정할 데이터 가져오기
  useEffect(() => {
    const fetchCustomDetail = async () => {
      try {
        const data = await getCustomDetail(cno); // API 호출로 기존 데이터 가져오기
        setTitle(data.title);
        setDescription(data.description);
        setSelectedTags(data.tags);
        setSelectedPlaces(data.places); // 기존 장소 설정
      } catch (error) {
        console.error("Error fetching custom detail:", error);
        alert("Failed to load the date course.");
      } finally {
        setLoading(false);
      }
    };

    fetchCustomDetail();
  }, [cno]);

  // 🔥 단계 변경 핸들러
  const handleNextStep = () => {
    if (currentStep === 1 && selectedPlaces.length < 2) {
      alert("장소를 최소 2개 이상 선택해야 합니다.");
      return;
    }
    setCurrentStep((prev) => prev + 1);
  };

  const handlePreviousStep = () => {
    setCurrentStep((prev) => prev - 1);
  };

  // 주소로 장소 검색
  const handleSearch = async () => {
    try {
      const data = await searchPlaces(address);
      setPlaces(data);
    } catch (error) {
      console.error("Error fetching places:", error);
      alert("Failed to fetch places. Please try again.");
    }
  };

  // 장소 선택
  const togglePlaceSelection = (place) => {
    const alreadySelected = selectedPlaces.find((p) => p.id === place.id);
    if (alreadySelected) {
      setSelectedPlaces(selectedPlaces.filter((p) => p.id !== place.id));
    } else {
      if (selectedPlaces.length >= 5) {
        alert("5개까지만 선택이 가능합니다.");
        return;
      }
      setSelectedPlaces([...selectedPlaces, place]);
    }
  };

  // 태그 선택/해제 핸들러
  const toggleTag = (tag) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(selectedTags.filter((t) => t !== tag));
    } else {
      setSelectedTags([...selectedTags, tag]);
    }
  };

  // 데이트 코스 수정
  const handleUpdate = async () => {
    if (!title || !description || selectedPlaces.length < 2) {
      alert("모든 입력사항을 채우고, 장소를 최소한 2곳을 선택하여 주십시오.");
      return;
    }

    const course = {
      title,
      description,
      places: selectedPlaces,
      tags: selectedTags,
    };

    try {
      await updateCustom(cno, course); // 수정 API 호출
      alert("코스가 정상적으로 수정되었습니다.");
      navigate(`/custom/read/${cno}`); // 수정 후 상세 페이지로 이동
    } catch (error) {
      console.error("Error updating date course:", error);
      alert("코스 수정에 실패하였습니다. 다시 시도 하여 주십시오.");
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      {currentStep === 1 && (
        <div>
          <h1>데이트 코스 수정하기</h1>
          <h3>장소를 선택해주세요</h3>
          <div>
            <label>장소 검색하기:</label>
            <input
              className="px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              type="text"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
            />
            <button
              className="px-4 py-2 bg-blue-500 text-white rounded"
              onClick={handleSearch}
            >
              검색
            </button>
          </div>

          {/* 선택된 장소 */}
          {selectedPlaces.length > 0 && (
            <div>
              <h3>선택된 장소</h3>
              <h5>최대 5개까지 선택할 수 있습니다.</h5>
              <div className="flex gap-4 list-none p-0">
                {selectedPlaces.map((place) => (
                  <Card className="w-[200px] h-[300px]">
                    <Card.Img variant="top" src={user} />
                    <Card.Body>
                      <Card.Title>{place.name}</Card.Title>
                    </Card.Body>
                  </Card>
                ))}
              </div>
            </div>
          )}

          {/* 검색 결과 */}
          {places.length > 0 && (
            <div>
              <h3>검색 결과</h3>
              <div className="grid grid-cols-5 gap-4 list-none p-0">
                {places.map((place) => (
                  <Card className="w-[300px] h-[300px]">
                    <Card.Img variant="top" src={user} />
                    <Card.Body>
                      <Card.Title>{place.name}</Card.Title>
                      <Card.Text>{place.description}</Card.Text>
                      <button
                        className={`px-4 py-2 rounded ${
                          selectedPlaces.find((p) => p.id === place.id)
                            ? "bg-red-500 text-white"
                            : "bg-green-500 text-white"
                        }`}
                        onClick={() => togglePlaceSelection(place)}
                      >
                        {selectedPlaces.find((p) => p.id === place.id)
                          ? "취소"
                          : "코스에 추가하기"}
                      </button>
                    </Card.Body>
                  </Card>
                ))}
              </div>
            </div>
          )}

          <button onClick={handleNextStep}>다음 단계로</button>
        </div>
      )}

      {currentStep === 2 && (
        <div>
          {/* 데이트 코스 등록 */}
          <div>
            <h1>데이트 코스 수정하기</h1>
            <label>코스 제목을 입력해주세요</label>
            <input
              className="px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />

            <label>코스에 대해 설명 해주세요</label>
            <textarea
              className="px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            ></textarea>

            <div className="mb-4">
              <label className="block mb-2 font-medium">
                누구랑 가는 코스인가요?
              </label>
              <div className="grid grid-cols-3 gap-2">
                {WITH_TAGS.map((tag) => (
                  <label key={tag} className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      value={tag}
                      checked={selectedTags.includes(tag)}
                      onChange={() => toggleTag(tag)}
                      className="form-checkbox h-4 w-4 text-blue-600"
                    />
                    <span className="text-sm">{tag}</span>
                  </label>
                ))}
              </div>
            </div>

            <div className="mb-4">
              <label className="block mb-2 font-medium">
                무엇을 하러 가는 코스인가요?
              </label>
              <div className="grid grid-cols-3 gap-2">
                {DO_TAGS.map((tag) => (
                  <label key={tag} className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      value={tag}
                      checked={selectedTags.includes(tag)}
                      onChange={() => toggleTag(tag)}
                      className="form-checkbox h-4 w-4 text-blue-600"
                    />
                    <span className="text-sm">{tag}</span>
                  </label>
                ))}
              </div>
            </div>
            <button onClick={handlePreviousStep}>이전 단계로</button>
            <button onClick={handleUpdate}>코스 수정하기</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default UpdateComponent;
