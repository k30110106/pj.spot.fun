import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useBasic } from "../context/BasicContext";
import { postLogoutApi } from "../../usr/login/api/LogoutApi";

const Aside = () => {
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
    <aside id="aside-component">
      사이드 메뉴영역
      <ul>
        <li>
          <Link to={"/"}>메인</Link>
        </li>
        {userInfo ? (
          <>
            <li>
              <span>{userInfo.nickname}님</span>
            </li>
            <li>
              <button type="button" onClick={handleLogout}>
                로그아웃
              </button>
            </li>
          </>
        ) : (
          <>
            <li>
              <Link to={"/login"}>로그인</Link>
            </li>
            <li>
              <Link to={"/signup"}>회원가입</Link>
            </li>
          </>
        )}
        <li>
          <Link to={"/mypage"}>마이페이지</Link>
        </li>
        <li>
          <Link to={"/feed"}>피드</Link>
        </li>
        <li>
          <Link to={"/boardmain"}>게시판</Link>
        </li>
      </ul>
    </aside>
  );
};

export default Aside;
