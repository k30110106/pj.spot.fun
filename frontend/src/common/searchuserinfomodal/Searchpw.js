import React, { useState, useEffect } from "react";
import {
  sendEmailVerificationApi,
  updatePasswordApi,
} from "../../usr/searchuserinfo/api/SearchUser";

const Searchpw = ({ onClose }) => {
  const [isEmailVerified, setIsEmailVerified] = useState(false); // 이메일 인증 상태
  const [verificationCode, setVerificationCode] = useState(""); // 사용자 입력 인증 코드
  const [serverCode, setServerCode] = useState(null); // 서버 생성 인증 코드
  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false); // 비밀번호 변경 모달 상태
  const [errors, setErrors] = useState({});
  const [isTouched, setIsTouched] = useState({});

  const [passwords, setPasswords] = useState({
    newPassword: "",
    confirmPassword: "",
  }); // 새 비밀번호와 확인 비밀번호 상태

  const [formData, setFormData] = useState({
    name: "",
    birthDate: "",
    userId: "",
    email: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setIsTouched((prevState) => ({ ...prevState, [name]: true }));
  };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswords({ ...passwords, [name]: value });
    setIsTouched((prevState) => ({ ...prevState, [name]: true }));
  };

  useEffect(() => {
    const newErrors = {};

    if (isTouched.name && !formData.name.match(/^[a-zA-Z가-힣]+$/)) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (isTouched.userId && !formData.userId.match(/^[a-zA-Z0-9]{4,12}$/)) {
      newErrors.userId = "아이디를 입력해주세요.";
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

  useEffect(() => {
    const newErrors = {};

    if (
      isTouched.newPassword &&
      !passwords.newPassword.match(
        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/
      )
    ) {
      newErrors.password = "비밀번호를 입력해주세요.";
    }

    if (
      isTouched.confirmPassword &&
      passwords.newPassword !== passwords.confirmPassword
    ) {
      newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    }

    setErrors(newErrors);
  }, [passwords, isTouched]);

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

  const handleSubmit = () => {
    const newErrors = {};
    if (!formData.name.match(/^[a-zA-Z가-힣]+$/)) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (!formData.userId.match(/^[a-zA-Z0-9]{4,12}$/)) {
      newErrors.userId = "아이디를 입력해주세요.";
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
    setIsPasswordModalOpen(true); // 비밀번호 변경 모달 열기
  };

  const handlePasswordSubmit = async () => {
    if (!passwords.newPassword || !passwords.confirmPassword) {
      alert("새 비밀번호와 비밀번호 확인을 모두 입력해주세요.");
      return;
    }

    if (passwords.newPassword !== passwords.confirmPassword) {
      alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
      return;
    }

    try {
      const responseData = await updatePasswordApi({
        userId: formData.userId,
        email: formData.email,
        newPassword: passwords.newPassword,
      });

      // 성공적으로 처리된 경우
      alert(responseData.message || "비밀번호가 성공적으로 변경되었습니다.");
      onClose(); // 전체 모달 닫기
    } catch (error) {
      // 실패 시 에러 메시지 표시
      alert(error.message || "비밀번호 변경 중 문제가 발생했습니다.");
    }
  };

  return (
    <div>
      {!isPasswordModalOpen ? (
        <>
          <h2 className="flex flex-col justify-center items-center text-2xl font-bold m-5">
            비밀번호 찾기
          </h2>
          <div>
            <p className="font-bold">아이디</p>
            <input
              type="text"
              name="userId"
              placeholder="영문, 숫자 포함 4~12자"
              onChange={handleInputChange}
              className="mt-1 p-2 mb-2 w-52 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />
            {errors.userId && <p style={{ color: "red" }}>{errors.userId}</p>}
          </div>
          <div>
            <p className="font-bold">이름</p>
            <input
              type="text"
              name="name"
              placeholder="한글 또는 영문"
              onChange={handleInputChange}
              className="mt-1 p-2 mb-2 w-52 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />
            {errors.name && <p style={{ color: "red" }}>{errors.name}</p>}
          </div>
          <div>
            <p className="font-bold">생년월일</p>
            <input
              type="date"
              name="birthDate"
              onChange={handleInputChange}
              className="mt-1 p-2 mb-2 w-52 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
            />
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
              name="verificationCode"
              placeholder="인증 코드 입력"
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
              className="bg-custom-cyan rounded-3xl mt-1 ml-2 p-2 w-32 hover:bg-emerald-400"
            >
              인증 확인
            </button>
          </div>
          <button
            onClick={handleSubmit}
            className="bg-custom-cyan rounded-3xl mt-1 ml-2 p-2 w-32 hover:bg-emerald-400"
          >
            비밀번호 찾기
          </button>
        </>
      ) : (
        <div>
          <h2 className="flex flex-col justify-center items-center text-2xl font-bold m-5">
            새 비밀번호 설정
          </h2>
          <div>
            <p className="font-bold">새 비밀번호</p>
            <input
              type="password"
              name="newPassword"
              placeholder="영문, 숫자, 특수문자 포함 8~16자"
              onChange={handlePasswordChange}
              className="mt-1 p-2 mb-2 w-52 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
              onBlur={() =>
                setIsTouched((prevState) => ({
                  ...prevState,
                  newPassword: true,
                }))
              }
            />
            {errors.password && (
              <p style={{ color: "red" }}>{errors.password}</p>
            )}
          </div>
          <div>
            <p className="font-bold">새 비밀번호 확인</p>
            <input
              type="password"
              name="confirmPassword"
              placeholder="새 비밀번호 확인"
              onChange={handlePasswordChange}
              className="mt-1 p-2 w-52 rounded-3xl border 
            focus:outline-none focus:ring-1 focus:border-custom-cyan focus:ring-custom-cyan bg-gray-200"
              onBlur={() =>
                setIsTouched((prevState) => ({
                  ...prevState,
                  confirmPassword: true,
                }))
              }
            />
            {errors.confirmPassword && (
              <p style={{ color: "red" }}>{errors.confirmPassword}</p>
            )}
          </div>
          <button
            onClick={handlePasswordSubmit}
            className="bg-custom-cyan rounded-3xl mt-1 ml-2 p-2 w-32 hover:bg-emerald-400"
          >
            비밀번호 변경
          </button>
        </div>
      )}
    </div>
  );
};

export default Searchpw;
