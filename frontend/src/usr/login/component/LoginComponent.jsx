import React, { useState, useEffect, useRef } from "react";
import { postLoginApi } from "../api/LoginApi";
import { useNavigate } from "react-router-dom";
import Searchid from "../../../common/searchuserinfomodal/Searchid";
import Searchpw from "../../../common/searchuserinfomodal/Searchpw";
import SearchModal from "../../../common/searchuserinfomodal/SearchModal";
import { useBasic } from "../../../common/context/BasicContext";
import { ReactComponent as Google } from "../../../common/img/Google.svg";
import { ReactComponent as Naver } from "../../../common/img/naver.svg";
import { ReactComponent as Kakao } from "../../../common/img/kakao.svg";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

const LoginComponent = () => {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [isIdModalOpen, setIsIdModalOpen] = useState(false);
  const [isPwModalOpen, setIsPwModalOpen] = useState(false);
  const [isTouched, setIsTouched] = useState({});
  const [showPassword, setShowPassword] = useState(false); // 비밀번호 표시 상태
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
    formData.append("provider", "LOCAL");

    try {
      const result = await postLoginApi(formData);
      if (result.status === 200 && result.data) {
        alert(`${result.data}님, 환영합니다!`);
        setUserInfo(result.data.userId);
        setUserInfo(result.data.nickname);
        setUserInfo(result.data);
        navigate("/");
      } else if (result.status === 403) {
        // 탈퇴된 회원 처리
        alert("탈퇴된 회원입니다.");
      } else {
        alert("로그인 실패. 아이디와 비밀번호를 확인해주세요.");
      }
    } catch (error) {
      alert("로그인 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  const handleSocialLogin = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const errorMessage = params.get("error_message");
    if (errorMessage) {
      alert(errorMessage);
      window.location.href = "/login"; // alert 후 로그인 페이지로 이동
    }
  }, []);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen py-5 bg-white">
      <h1 className="mb-6 text-2xl font-bold">로그인</h1>
      <form
        className="flex flex-col items-center"
        id="login-form"
        onSubmit={(e) => {
          e.preventDefault(); // 기본 제출 동작 방지
          doLogin(); // 로그인 함수 실행
        }}
      >
        <div className="mb-4">
          <input
            type="text"
            name="userId"
            ref={idInputRef}
            value={userId}
            placeholder="아이디를 입력해주세요"
            onChange={(e) => {
              setUserId(e.target.value);
              setIsTouched((prev) => ({ ...prev, userId: true }));
            }}
            onBlur={() => setIsTouched((prev) => ({ ...prev, userId: true }))}
            className="mt-2 p-2 w-80 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
          />
          {isTouched.userId && errors.userId && (
            <p className="text-red-500">{errors.userId}</p>
          )}
        </div>
        <div className="mb-6 relative w-80">
          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              ref={passwordInputRef}
              value={password}
              placeholder="비밀번호를 입력해주세요"
              onChange={(e) => {
                setPassword(e.target.value);
                setIsTouched((prev) => ({ ...prev, password: true }));
              }}
              onBlur={() =>
                setIsTouched((prev) => ({ ...prev, password: true }))
              }
              className="mt-2 p-2 w-80 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />

            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)} // 비밀번호 토글 상태 변경
              className="absolute top-1/2 right-3 transform -translate-y-1/2 text-gray-500 hover:text-gray-700"
            >
              {showPassword ? (
                <Visibility fontSize="small" /> // 눈 아이콘
              ) : (
                <VisibilityOff fontSize="small" /> // 눈 가림 아이콘
              )}
            </button>
          </div>
          {isTouched.password && errors.password && (
            <p className="text-red-500">{errors.password}</p>
          )}
        </div>
        <button
          type="submit"
          className="mb-2 p-2 w-80 bg-custom-cyan  text-white rounded-3xl cursor-pointer hover:bg-emerald-500"
        >
          로그인
        </button>
      </form>

      <div id="search-user-info" className="mt-6 text-center">
        <div>
          <b
            className="ml-4 mr-2 cursor-pointer text-gray-400 hover:text-custom-cyan"
            onClick={() => setIsIdModalOpen(true)}
          >
            아이디 찾기
          </b>
          <span className="text-gray-300"> | </span>
          <b
            className="cursor-pointer ml-2 text-gray-400 hover:text-custom-cyan"
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

      <div id="social-login" className="mt-8 text-center">
        <h2 className="mb-4 text-xl font-bold">SNS 로그인</h2>
        <div className="flex justify-center space-x-4">
          <div className="text-center">
            <button
              onClick={() => handleSocialLogin("google")}
              className="bg-custom-cyan text-white rounded-full w-12 h-12 flex items-center justify-center cursor-pointer"
            >
              <Google />
            </button>
            <div className="mt-2 text-xs">구글</div>
          </div>
          <div className="text-center">
            <button
              onClick={() => handleSocialLogin("kakao")}
              className="bg-custom-cyan text-white rounded-full w-12 h-12 flex items-center justify-center cursor-pointer"
            >
              <Kakao />
            </button>
            <div className="mt-2 text-xs">카카오</div>
          </div>
          <div className="text-center">
            <button
              onClick={() => handleSocialLogin("naver")}
              className="bg-custom-cyan text-white rounded-full w-12 h-12 flex items-center justify-center cursor-pointer"
            >
              <Naver />
            </button>
            <div className="mt-2 text-xs">네이버</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
