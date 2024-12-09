import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useBasic } from "../context/BasicContext";
import { postLogoutApi } from "../../usr/login/api/LogoutApi";
import Logo from "../img/FunSpotLogo.jpg"

const Header = () => {
  const { userInfo, setUserInfo } = useBasic();
  const navigate = useNavigate();

  const handleLogout = async () => {
    const result = await postLogoutApi();
    if (result.success) {
      setUserInfo(null); // 상태 초기화
      alert(result.message);
      navigate("/"); // 메인 페이지로 이동
    } else {
      alert(result.message);
    }
  };

  return (
    <header className="bg-white text-black p-4 mb-4 shadow-md rounded-md">
      <div className="container mx-auto flex justify-between items-center">
        <a href="/">
          {/* <h1 className="text-3xl font-bold">FunSpot</h1> */}
          <img src={Logo} alt="펀스팟로고" height='px' width='150px' />
        </a>
        <div>
          {userInfo ? (
            <ul className="flex items-center">
              <li className="list-none">
                <span className="mr-3">{userInfo.nickname}님</span>
              </li>
              {/* <span className="p-1 cursor-default">|</span> */}
              <li>
                <button type="button" onClick={handleLogout}>
                  로그아웃
                </button>
              </li>
            </ul>
          ) : (
            <ul className="flex items-center">
              <li>
                <Link to={"/login"} className="mr-2">
                  로그인
                </Link>
              </li>
              {/* <span className="p-1 cursor-default">|</span> */}
              <li>
                <Link to={"/signup"}>회원가입</Link>
              </li>
            </ul>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
