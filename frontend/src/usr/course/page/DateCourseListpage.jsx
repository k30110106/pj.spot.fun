import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import BasicLayout from "../../../common/layout/BasicLayout";

const DateCourseListPage = () => {
  const [courses, setCourses] = useState([]); // 코스 목록 상태
  const [newCourse, setNewCourse] = useState({
    name: "",
    location: "",
    description: "",
    fixed: false,
  });

  const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리
  const navigate = useNavigate(); // 페이지 이동을 위한 훅

  // 서버에서 코스 목록을 가져오는 API 호출
  useEffect(() => {
    axios.get("http://localhost:8080/api/usr/datecourse/all")
      .then((response) => {
        setCourses(response.data); // 데이터를 상태에 저장
        console.log("Fetched courses: ", response.data); // 데이터 확인
      })
      .catch((error) => {
        console.error("Error fetching date courses:", error);
      });

    // 로그인 상태 확인 (예시, 실제로는 로그인 상태를 API 또는 쿠키로 확인)
    const token = localStorage.getItem("access_token");
    setIsLoggedIn(!!token); // 토큰이 존재하면 로그인된 상태로 간주
  }, []);

  // 코스 추가 핸들러
  const handleAddCourse = (e) => {
    e.preventDefault(); // 폼 제출 시 페이지 리로딩 방지

    // 새로운 코스를 서버로 POST 요청
    axios.post("http://localhost:8080/api/usr/datecourse", newCourse)
      .then((response) => {
        console.log("Added new course: ", response.data); // 추가된 코스 확인
        // 서버에서 코스를 추가한 후, courses 배열을 갱신
        setCourses((prevCourses) => [...prevCourses, response.data]);
        setNewCourse({
          name: "",
          location: "",
          description: "",
          fixed: false,
        }); // 폼 초기화
      })
      .catch((error) => {
        console.error("Error adding date course:", error);
      });
  };

  // 입력 필드 변경 핸들러
  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setNewCourse({
      ...newCourse,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  // 새로운 코스 추가 페이지로 이동
  const navigateToAddCoursePage = () => {
    if (isLoggedIn) {
      console.log("Navigating to /add-course..");
      navigate("/add-course"); // 로그인 상태에서만 페이지로 이동
    } else {
      alert("로그인 후 코스를 추가할 수 있습니다.");
    }
  };

  return (
    <BasicLayout>
      <div className="max-w-4xl mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-center text-gray-800 mb-8">데이트 코스 목록</h1>

        {/* 새로운 코스 추가 버튼 */}
        <div className="mb-8 text-center">
          <button
            onClick={navigateToAddCoursePage}
            disabled={!isLoggedIn} // 로그인하지 않으면 비활성화
            className={`w-full bg-green-500 text-white py-2 px-4 rounded-md hover:bg-green-600 transition duration-200 cursor-pointer 
            ${!isLoggedIn ? 'opacity-50 cursor-not-allowed' : ''}`}
          >
            새로운 코스 추가
          </button>
        </div>

        {/* 코스 목록 표시 */}
        {Array.isArray(courses) && courses.length > 0 ? (
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-semibold mb-6 text-gray-700">등록된 코스</h2>
            <ul className="space-y-4">
              {courses.map((course) => (
                <li key={course.id} className="border-b pb-4">
                  <h3 className="text-xl font-semibold text-gray-800">{course.name}</h3>
                  <p className="text-gray-700">{course.location}</p>
                  <p className="text-gray-600">{course.description}</p>
                  <p className="text-gray-500">
                    {course.fixed ? "고정된 코스" : "변동 가능한 코스"}
                  </p>
                </li>
              ))}
            </ul>
          </div>
        ) : (
          <p className="text-center text-gray-500">등록된 코스가 없습니다.</p>
        )}
      </div>
    </BasicLayout>
  );
};

export default DateCourseListPage;
