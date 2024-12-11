import React from "react";
import { Link } from "react-router-dom";

const Dropdown = ({ handleLogout }) => {
  return (
    <div className="absolute mt-28 w-28 bg-white border border-gray-200 rounded-md shadow-lg z-50">
      <ul className="py-2">
        <li>
          <Link
            to="/mypage"
            className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
          >
            마이페이지
          </Link>
        </li>
        <li>
          <button
            type="button"
            onClick={handleLogout}
            className="w-full text-left block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
          >
            로그아웃
          </button>
        </li>
      </ul>
    </div>
  );
};

export default Dropdown;
