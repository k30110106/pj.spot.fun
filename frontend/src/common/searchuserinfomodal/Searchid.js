import React from "react";
import { useState, useEffect } from "react";
import {
  sendEmailVerificationApi,
  searchIdApi,
} from "../../usr/searchuserinfo/api/SearchUser";

const Searchid = ({ onClose, focusIdField }) => {
  const [isEmailVerified, setIsEmailVerified] = useState(false); // 이메일 인증 상태
  const [verificationCode, setVerificationCode] = useState(""); // 사용자 입력 인증 코드
  const [serverCode, setServerCode] = useState(null); // 서버 생성 인증 코드
  const [errors, setErrors] = useState({});
  const [isTouched, setIsTouched] = useState({});

  const [formData, setFormData] = useState({
    name: "",
    birthDate: "",
    email: "",
  });
  const [searchResult, setSearchResult] = useState(null); // 아이디 검색 결과
  const [isSearching, setIsSearching] = useState(false); // 검색 중 상태

  useEffect(() => {
    const newErrors = {};

    if (isTouched.name && !formData.name.match(/^[a-zA-Z가-힣]+$/)) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (
      isTouched.email &&
      !formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)
    ) {
      newErrors.email = "유효한 이메일 형식이 아닙니다.";
    }

    if (isTouched.birthDate && !formData.birthDate) {
      newErrors.birthDate = "생년월일을 입력해주세요.";
    }

    setErrors(newErrors);
  }, [formData, isTouched]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setIsTouched((prevState) => ({ ...prevState, [name]: true }));
  };

  const handleSendEmailVerification = async () => {
    if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
      alert("유효한 이메일 주소를 입력해주세요.");
      return;
    }

    try {
      const response = await sendEmailVerificationApi({
        email: formData.email,
      });
      if (response.status === 200) {
        setServerCode(response.data.verificationCode); // 서버에서 보낸 인증 코드 저장
        alert("인증 코드가 이메일로 전송되었습니다.");
      } else {
        alert("이메일 인증 요청 실패. 다시 시도해주세요.");
      }
    } catch (error) {
      alert("서버와의 통신 중 문제가 발생했습니다.");
    }
  };

  const handleVerifyCode = () => {
    if (verificationCode === serverCode) {
      setIsEmailVerified(true);
      alert("이메일 인증이 완료되었습니다.");
    } else {
      alert("인증 코드가 일치하지 않습니다.");
    }
  };

  const handleSubmit = async () => {
    const newErrors = {};
    if (!formData.name.match(/^[a-zA-Z가-힣]+$/)) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
      newErrors.email = "유효한 이메일 형식이 아닙니다.";
    }

    if (!formData.birthDate) {
      newErrors.birthDate = "생년월일을 입력해주세요.";
    }

    // 에러가 있을 경우 처리
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors); // 에러 메시지 상태 업데이트
      alert("입력되지 않은 항목이 있습니다. 다시 확인해주세요."); // 경고창 띄움
      return;
    }

    if (!isEmailVerified) {
      alert("이메일 인증을 완료해주세요.");
      return;
    }

    try {
      setIsSearching(true);
      const response = await searchIdApi(formData);
      if (response.status === 200) {
        setSearchResult(response.data); // 검색 결과 저장
      } else {
        alert("아이디를 찾을 수 없습니다.");
      }
    } catch (error) {
      alert("아이디 검색 중 문제가 발생했습니다.");
    } finally {
      setIsSearching(false);
    }
  };

  return (
    <div >
      <h2 className="flex flex-col justify-center items-center text-2xl font-bold m-5">아이디 찾기</h2>
      {!searchResult ? (
        <>
          <div>
            <p className="font-bold">이름</p>
            <input
              type="text"
              name="name"
              placeholder="이름"
              onChange={handleInputChange}
              className="mt-1 p-2 w-52 mb-2 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />
            {errors.name && <p style={{ color: "red" }}>{errors.name}</p>}
          </div>
          <div>
            <p className="font-bold">생년월일</p>
            <input type="date" name="birthDate" onChange={handleInputChange}
              className="mt-1 p-2 w-52 mb-2 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"/>
            {errors.birthDate && (
              <p style={{ color: "red" }}>{errors.birthDate}</p>
            )}
          </div>
          <div>
            <p className="font-bold">이메일</p>
            <input
              type="email"
              name="email"
              placeholder="example@naver.com"
              onChange={handleInputChange}
              disabled={isEmailVerified}
              className="mt-1 p-2 w-52 mb-2 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />
            <button
              type="button"
              onClick={handleSendEmailVerification}
              disabled={isEmailVerified}
              className="bg-custom-cyan rounded-3xl mt-1 ml-2 p-2 w-32 hover:bg-emerald-400"
            >
              메일 전송
            </button>
            {errors.email && <p style={{ color: "red" }}>{errors.email}</p>}
          </div>
          <div>
            <p className="font-bold">인증 코드</p>
            <input
              type="text"
              placeholder="인증코드 입력"
              name="verificationCode"
              value={verificationCode}
              onChange={(e) => setVerificationCode(e.target.value)}
              disabled={isEmailVerified}
              className="mt-1 p-2 mb-2 w-52 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />
            <button
              type="button"
              onClick={handleVerifyCode}
              disabled={isEmailVerified}
              className="bg-custom-cyan rounded-3xl mt-1 mb-1 ml-2 p-2 w-32 justify-center  hover:bg-emerald-400"
            >
              인증 확인
            </button>
          </div>
          <button onClick={handleSubmit} disabled={isSearching}
            className="bg-custom-cyan rounded-3xl mt-1 mb-1 ml-2 p-2 w-32 justify-center  hover:bg-emerald-400 flex"
          >
            {isSearching ? "검색 중..." : "아이디 찾기"}
          </button>
        </>
      ) : (
        <div>
          {searchResult.userId ? (
            <p>
              찾으시는 아이디는 <strong>{searchResult.userId}</strong> 입니다.
            </p>
          ) : (
            <p>입력하신 정보와 일치하는 아이디가 없습니다.</p>
          )}
          <button
            onClick={() => {
              onClose();
              focusIdField();
            }}
          >
            로그인하러 가기
          </button>
        </div>
      )}
    </div>
  );
};

export default Searchid;
