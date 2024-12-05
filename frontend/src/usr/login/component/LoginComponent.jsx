import React, { useState, useEffect, useRef } from "react";
import { postLoginApi } from "../api/LoginApi";
import { useNavigate } from "react-router-dom";
import Searchid from "../../../common/searchuserinfomodal/Searchid";
import Searchpw from "../../../common/searchuserinfomodal/Searchpw";
import SearchModal from "../../../common/searchuserinfomodal/SearchModal";
import { useBasic } from "../../../common/context/BasicContext";

const LoginComponent = () => {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [isIdModalOpen, setIsIdModalOpen] = useState(false);
  const [isPwModalOpen, setIsPwModalOpen] = useState(false);
  // const [nickname, setNickname] = useState(null);
  const [isTouched, setIsTouched] = useState({});
  const { setUserInfo } = useBasic();

  const idInputRef = useRef(null);
  const passwordInputRef = useRef(null);

  const navigate = useNavigate();

  const validateInputs = () => {
    const newErrors = {};
    if (!userId.trim()) {
      newErrors.userId = "아이디를 입력해주세요.";
    }
    if (!password.trim()) {
      newErrors.password = "비밀번호를 입력해주세요.";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const doLogin = async () => {
    if (!validateInputs()) {
      if (!userId.trim()) {
        alert("아이디를 입력해주세요.");
        idInputRef.current?.focus();
      } else if (!password.trim()) {
        alert("비밀번호를 입력해주세요.");
        passwordInputRef.current?.focus();
      }
      return;
    }

    const formData = new FormData();
    formData.append("userId", userId);
    formData.append("password", password);

    try {
      const result = await postLoginApi(formData);
      if (result.status === 200 && result.data) {
        alert(`${result.data}님, 환영합니다!`);
        // console.log("Before setNickname:", result.data); // 디버깅
        setUserInfo(result.data.userId); // userIdx 업데이트
        setUserInfo(result.data.nickname); // 닉네임 업데이트
        setUserInfo(result.data);
        navigate("/");
        // console.log("After setNickname:", nickname); // 디버깅
      } else {
        alert("로그인 실패. 아이디와 비밀번호를 확인해주세요.");
      }
    } catch (error) {
      alert("로그인 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  const handleCancle = () => {
    navigate("/");
  };

  const handleSocialLogin = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  return (
    <div>
      <form id="login-form">
        <div>
          <label className="font-bold text-xl">아이디</label>
          <input
            type="text"
            name="userId"
            ref={idInputRef}
            value={userId}
            placeholder="ID"
            onChange={(e) => {
              setUserId(e.target.value);
              setIsTouched((prev) => ({ ...prev, userId: true }));
            }}
            onBlur={() => setIsTouched((prev) => ({ ...prev, userId: true }))}
          />
          {isTouched.userId && errors.userId && (
            <p style={{ color: "red" }}>{errors.userId}</p>
          )}
        </div>
        <div>
          <label>비밀번호</label>
          <input
            type="password"
            name="password"
            ref={passwordInputRef}
            value={password}
            placeholder="Password"
            onChange={(e) => {
              setPassword(e.target.value);
              setIsTouched((prev) => ({ ...prev, password: true }));
            }}
            onBlur={() => setIsTouched((prev) => ({ ...prev, password: true }))}
          />
          {isTouched.password && errors.password && (
            <p style={{ color: "red" }}>{errors.password}</p>
          )}
        </div>
        <button type="button" onClick={doLogin}>
          로그인
        </button>
        <button type="button" onClick={handleCancle}>
          취소
        </button>
      </form>
      <div id="search-user-info">
        <div>
          <b
            style={{ marginLeft: "15px", cursor: "pointer" }}
            onClick={() => setIsIdModalOpen(true)}
          >
            아이디 찾기
          </b>
        </div>
        <div>
          <b
            style={{ cursor: "pointer" }}
            onClick={() => setIsPwModalOpen(true)}
          >
            비밀번호 찾기
          </b>
        </div>
      </div>

      {isIdModalOpen && (
        <SearchModal
          isOpen={isIdModalOpen}
          onClose={() => setIsIdModalOpen(false)}
        >
          <Searchid onClose={() => setIsIdModalOpen(false)} />
        </SearchModal>
      )}

      {isPwModalOpen && (
        <SearchModal
          isOpen={isPwModalOpen}
          onClose={() => setIsPwModalOpen(false)}
        >
          <Searchpw onClose={() => setIsPwModalOpen(false)} />
        </SearchModal>
      )}

      <div id="social-login">
        <button onClick={() => handleSocialLogin("google")}>구글 로그인</button>
        <button onClick={() => handleSocialLogin("kakao")}>
          카카오 로그인
        </button>
        <button onClick={() => handleSocialLogin("naver")}>
          네이버 로그인
        </button>
      </div>
    </div>
  );
};

export default LoginComponent;
