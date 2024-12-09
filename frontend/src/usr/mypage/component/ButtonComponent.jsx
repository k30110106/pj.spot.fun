import React from "react";

const ButtonComponent = () => {
  return (
    <>
      <div className="mt-4">
        <div className="mt-2 flex space-x-4">
          <button className="bg-blue-500 text-white py-1 px-4 rounded-full text-sm hover:bg-blue-600">
            팔로우
          </button>
          <button className="bg-gray-200 text-gray-800 py-1 px-4 rounded-full text-sm hover:bg-gray-300">
            메시지
          </button>
        </div>
      </div>

      <div className="mt-4">
        <div className="mt-2 flex space-x-4">
          <button className="bg-blue-500 text-white py-1 px-4 rounded-full text-sm hover:bg-blue-600">
            프로필 편집
          </button>
          <button className="bg-gray-200 text-gray-800 py-1 px-4 rounded-full text-sm hover:bg-gray-300">
            버튼2
          </button>
        </div>
      </div>
    </>
  );
};

export default ButtonComponent;
