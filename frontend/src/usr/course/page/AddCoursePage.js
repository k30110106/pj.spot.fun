import React, { useState } from "react";
import axios from "axios";
import BasicLayout from "../../../common/layout/BasicLayout";

const AddCoursePage = () => {
  const [newCourse, setNewCourse] = useState({
    name: "",
    location: "",
    description: "",
    fixed: false,
  });

  // 코스 추가 핸들러
  const handleAddCourse = (e) => {
    e.preventDefault();

    axios.post("http://localhost:8080/api/usr/datecourse", newCourse)
      .then((response) => {
        alert("새로운 코스가 추가되었습니다!");
        // 추가 후 코스 목록 페이지로 리디렉션
        window.location.href = "/";
      })
      .catch((error) => {
        console.error("Error adding date course:", error);
        alert("코스를 추가하는 데 문제가 발생했습니다.");
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

  return (
    <BasicLayout>
      <div className="max-w-4xl mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-center text-gray-800 mb-8">새로운 코스 추가</h1>

        <form onSubmit={handleAddCourse} className="bg-white p-6 rounded-lg shadow-lg">
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-600">코스 이름</label>
            <input
              type="text"
              name="name"
              value={newCourse.name}
              onChange={handleInputChange}
              required
              className="mt-2 p-2 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-600">위치</label>
            <input
              type="text"
              name="location"
              value={newCourse.location}
              onChange={handleInputChange}
              required
              className="mt-2 p-2 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-600">설명</label>
            <input
              type="text"
              name="description"
              value={newCourse.description}
              onChange={handleInputChange}
              required
              className="mt-2 p-2 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-600">고정된 코스</label>
            <input
              type="checkbox"
              name="fixed"
              checked={newCourse.fixed}
              onChange={handleInputChange}
              className="mt-2"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-200"
          >
            코스 추가
          </button>
        </form>
      </div>
    </BasicLayout>
  );
};

export default AddCoursePage;
