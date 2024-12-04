import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  postSignupApi,
  checkDuplicateApi,
  sendEmailVerificationApi,
} from "../api/SignupApi";
import AddressModal from "../../../common/signupmodal/AddressModal";
import AlertModal from "../../../common/signupmodal/AlertModal";

const SignupComponent = () => {
  const [formData, setFormData] = useState({
    userId: "",
    name: "",
    birthDate: "",
    nickname: "",
    password: "",
    confirmPassword: "",
    email: "",
    phone: "",
    address: "",
    detaileAdd: "",
    zonecode: "",
  });

  const [errors, setErrors] = useState({});
  const [isTouched, setIsTouched] = useState({});
  const [addressModalOpen, setAddressModalOpen] = useState(false);
  const [alertModalConfig, setAlertModalConfig] = useState({
    isOpen: false,
    message: "",
    callback: null,
  });
  const [isUserIdChecked, setIsUserIdChecked] = useState(false); // 아이디 중복 확인 상태
  const [isNicknameChecked, setIsNicknameChecked] = useState(false); // 닉네임 중복 확인 상태
  const [isEmailVerified, setIsEmailVerified] = useState(false); // 이메일 인증 상태
  const [verificationCode, setVerificationCode] = useState(""); // 사용자 입력 인증 코드
  const [serverCode, setServerCode] = useState(null); // 서버 생성 인증 코드
  const [callback, setCallback] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const newErrors = {};

    if (isTouched.userId && !formData.userId.match(/^[a-zA-Z0-9]{4,12}$/)) {
      newErrors.userId = "아이디를 입력해주세요.";
    }

    if (isTouched.name && !formData.name.match(/^[a-zA-Z가-힣]+$/)) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (
      isTouched.nickname &&
      !formData.nickname.match(/^[a-zA-Z0-9가-힣]{1,12}$/)
    ) {
      newErrors.nickname = "닉네임을 입력해주세요.";
    }

    const phoneRegex = /^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$/;
    if (isTouched.phone && !phoneRegex.test(formData.phone)) {
      newErrors.phone = "유효한 핸드폰 번호를 입력해주세요.";
    }

    if (
      isTouched.email &&
      !formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)
    ) {
      newErrors.email = "유효한 이메일 형식이 아닙니다.";
    }

    if (
      isTouched.password &&
      !formData.password.match(
        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/
      )
    ) {
      newErrors.password = "비밀번호를 입력해주세요.";
    }

    if (
      isTouched.confirmPassword &&
      formData.password !== formData.confirmPassword
    ) {
      newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    }

    if (isTouched.birthDate && !formData.birthDate) {
      newErrors.birthDate = "생년월일을 입력해주세요.";
    }

    if (isTouched.address && !formData.address) {
      newErrors.address = "주소를 입력해주세요.";
    }

    if (isTouched.zonecode && !formData.zonecode) {
      newErrors.zonecode = "우편번호를 입력해주세요.";
    }

    setErrors(newErrors);
  }, [formData, isTouched]);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData({ ...formData, [name]: value });

    // 값이 변경되면 중복 확인 상태 초기화
    if (name === "userId") setIsUserIdChecked(false);
    if (name === "nickname") setIsNicknameChecked(false);

    if (name === "userId") {
      const filteredValue = value.replace(/[^a-zA-Z0-9]/g, "");
      setFormData({ ...formData, [name]: filteredValue });
    } else if (name === "phone") {
      const formattedPhone = autoHyphenPhone(value);
      setFormData({ ...formData, [name]: formattedPhone });
    } else {
      setFormData({ ...formData, [name]: value });
    }

    setIsTouched((prev) => ({ ...prev, [name]: true }));
  };

  const handleSendEmailVerification = async () => {
    if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
      setAlertModalConfig({
        isOpen: true,
        message: "유효한 이메일 주소를 입력해주세요.",
      });
      return;
    }

    try {
      const response = await sendEmailVerificationApi({
        email: formData.email,
      });
      if (response.status === 200) {
        setServerCode(response.data.verificationCode); // 서버에서 보낸 인증 코드 저장
        setAlertModalConfig({
          isOpen: true,
          message: "인증 코드가 이메일로 전송되었습니다.",
        });
      } else {
        setAlertModalConfig({
          isOpen: true,
          message: "이메일 인증 요청 실패. 다시 시도해주세요.",
        });
      }
    } catch (error) {
      setAlertModalConfig({
        isOpen: true,
        message: "서버와의 통신 중 문제가 발생했습니다.",
      });
    }
  };

  const handleVerifyCode = () => {
    if (verificationCode === serverCode) {
      setIsEmailVerified(true);
      setAlertModalConfig({
        isOpen: true,
        message: "이메일 인증이 완료되었습니다.",
      });
    } else {
      setAlertModalConfig({
        isOpen: true,
        message: "인증 코드가 일치하지 않습니다.",
      });
    }
  };

  const handleDuplicateCheck = async (field) => {
    try {
      const value = formData[field];
      if (!value) {
        setAlertModalConfig({
          isOpen: true,
          message: `${
            field === "userId" ? "아이디" : "닉네임"
          }를(을) 입력해주세요.`,
        });
        return;
      }

      const response = await checkDuplicateApi({ field, value });
      if (response.data.isDuplicate) {
        setAlertModalConfig({
          isOpen: true,
          message: `${value}은(는) 중복된 ${
            field === "userId" ? "아이디" : "닉네임"
          }입니다.`,
        });
        if (field === "userId") setIsUserIdChecked(false);
        if (field === "nickname") setIsNicknameChecked(false);
      } else {
        setAlertModalConfig({
          isOpen: true,
          message: `${value}은(는) 사용 가능한 ${
            field === "userId" ? "아이디" : "닉네임"
          }입니다.`,
        });
        if (field === "userId") setIsUserIdChecked(true);
        if (field === "nickname") setIsNicknameChecked(true);
      }
    } catch (error) {
      setAlertModalConfig({
        isOpen: true,
        message: "중복 확인 중 문제가 발생했습니다.",
      });
    }
  };

  const autoHyphenPhone = (phone) => {
    return phone
      .replace(/[^0-9]/g, "")
      .replace(/^(\d{3})(\d{3,4})(\d{4})$/, "$1-$2-$3")
      .replace(/(\-{1,2})$/g, "");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isEmailVerified) {
      setAlertModalConfig({
        isOpen: true,
        message: "이메일 인증을 완료해주세요.",
      });
      return;
    }

    if (!isUserIdChecked) {
      setAlertModalConfig({
        isOpen: true,
        message: "아이디 중복 확인을 해주세요.",
      });
      return;
    }

    if (!isNicknameChecked) {
      setAlertModalConfig({
        isOpen: true,
        message: "닉네임 중복 확인을 해주세요.",
      });
      return;
    }

    const errorFields = Object.keys(errors).filter((key) => errors[key]);
    if (errorFields.length > 0) {
      const firstErrorField = errorFields[0];
      setAlertModalConfig({
        isOpen: true,
        message: errors[firstErrorField],
        field: firstErrorField,
      });
      return;
    }

    try {
      const response = await postSignupApi(formData);
      if (response.status === 200) {
        setCallback(() => () => navigate("/login")); // Callback 상태로 분리
        setAlertModalConfig({
          isOpen: true,
          message: response.data.message,
        });
      } else {
        const { field, message } = response.data;
        setAlertModalConfig({
          isOpen: true,
          message: message || "회원가입에 실패했습니다.",
          field,
        });
      }
    } catch (error) {
      setAlertModalConfig({
        isOpen: true,
        message: "서버와 통신 중 문제가 발생했습니다.",
        field: "",
      });
    }
  };

  const handleCancel = () => {
    navigate("/");
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <h2>회원가입</h2>

        <div>
          <label>아이디: </label>
          <input
            type="text"
            name="userId"
            placeholder="영문, 숫자 포함 4~12자"
            value={formData.userId}
            onChange={handleChange}
          />
          <button type="button" onClick={() => handleDuplicateCheck("userId")}>
            중복 확인
          </button>
          {errors.userId && <p style={{ color: "red" }}>{errors.userId}</p>}
        </div>

        <div>
          <label>비밀번호: </label>
          <input
            type="password"
            name="password"
            placeholder="영문, 숫자, 특수문자 포함 8~16자"
            value={formData.password}
            onChange={handleChange}
          />
          {errors.password && <p style={{ color: "red" }}>{errors.password}</p>}
        </div>

        <div>
          <label>비밀번호 확인: </label>
          <input
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
          />
          {errors.confirmPassword && (
            <p style={{ color: "red" }}>{errors.confirmPassword}</p>
          )}
        </div>

        <div>
          <label>이름: </label>
          <input
            type="text"
            name="name"
            placeholder="한글 또는 영문"
            value={formData.name}
            onChange={handleChange}
          />
          {errors.name && <p style={{ color: "red" }}>{errors.name}</p>}
        </div>

        <div>
          <label>생년월일: </label>
          <input
            type="date"
            name="birthDate"
            value={formData.birthDate}
            onChange={handleChange}
          />
          {errors.birthDate && (
            <p style={{ color: "red" }}>{errors.birthDate}</p>
          )}
        </div>

        <div>
          <label>닉네임: </label>
          <input
            type="text"
            name="nickname"
            placeholder="한글, 영문, 숫자 포함 4~12자"
            value={formData.nickname}
            onChange={handleChange}
          />
          <button
            type="button"
            onClick={() => handleDuplicateCheck("nickname")}
          >
            중복 확인
          </button>
          {errors.nickname && <p style={{ color: "red" }}>{errors.nickname}</p>}
        </div>

        <div>
          <label>핸드폰: </label>
          <input
            type="text"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            maxLength="13"
          />
          {errors.phone && <p style={{ color: "red" }}>{errors.phone}</p>}
        </div>

        <div>
          <label>이메일: </label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            disabled={isEmailVerified} // 이메일 인증 완료 시 비활성화
          />
          <button
            type="button"
            onClick={handleSendEmailVerification}
            disabled={isEmailVerified} // 이메일 인증 완료 시 버튼 비활성화
          >
            인증 메일 보내기
          </button>
          {errors.email && <p style={{ color: "red" }}>{errors.email}</p>}
        </div>

        <div>
          <label>인증 코드: </label>
          <input
            type="text"
            placeholder="인증코드 입력"
            value={verificationCode}
            onChange={(e) => setVerificationCode(e.target.value)}
            disabled={isEmailVerified} // 이메일 인증 완료 시 비활성화
          />
          <button
            type="button"
            onClick={handleVerifyCode}
            disabled={isEmailVerified} // 이메일 인증 완료 시 버튼 비활성화
          >
            인증 코드 확인
          </button>
        </div>

        <div>
          <label>우편번호: </label>
          <div>
            <input
              type="text"
              name="zonecode"
              value={formData.zonecode}
              readOnly
            />
            <button type="button" onClick={() => setAddressModalOpen(true)}>
              주소 찾기
            </button>
          </div>
          {errors.zonecode && <p style={{ color: "red" }}>{errors.zonecode}</p>}
        </div>

        <div>
          <label>주소: </label>
          <input type="text" name="address" value={formData.address} readOnly />
          {errors.address && <p style={{ color: "red" }}>{errors.address}</p>}
        </div>

        <div>
          <label>상세주소: </label>
          <input
            type="text"
            name="detaileAdd"
            value={formData.detaileAdd}
            onChange={handleChange}
          />
        </div>

        <button type="submit">가입하기</button>
        <button type="button" onClick={handleCancel}>
          취소
        </button>
      </form>

      <AddressModal
        isOpen={addressModalOpen}
        onClose={() => setAddressModalOpen(false)}
        onComplete={(data) => {
          setFormData({
            ...formData,
            address: data.address,
            zonecode: data.zonecode,
          });
          setAddressModalOpen(false);
        }}
      />

      <AlertModal
        isOpen={alertModalConfig.isOpen}
        message={alertModalConfig.message}
        onClose={() => {
          console.log("AlertModal onClose triggered");

          setAlertModalConfig({ isOpen: false, message: "", field: "" });

          if (callback) {
            console.log("Executing callback");
            callback(); // 분리된 상태에서 callback 호출
          }
        }}
      />
    </div>
  );
};

export default SignupComponent;
