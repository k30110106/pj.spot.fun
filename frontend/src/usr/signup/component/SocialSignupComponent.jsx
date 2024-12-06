import React, { useEffect, useState } from "react";
import { postSocialSignupApi, checkDuplicateApi } from "../api/SignupApi";
import { useNavigate } from "react-router-dom";
import AddressModal from "../../../common/signupmodal/AddressModal";
import AlertModal from "../../../common/signupmodal/AlertModal";
import axios from "axios";

const SocialSignupComponent = () => {
  const [errors, setErrors] = useState({});
  const [isTouched, setIsTouched] = useState({});
  const navigate = useNavigate();
  const [callback, setCallback] = useState(null);
  const [alertModalConfig, setAlertModalConfig] = useState({
    isOpen: false,
    message: "",
    callback: null,
  });
  const [isUserIdChecked, setIsUserIdChecked] = useState(false); // 아이디 중복 확인 상태
  const [isNicknameChecked, setIsNicknameChecked] = useState(false); // 닉네임 중복 확인 상태
  const [addressModalOpen, setAddressModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    userId: "",
    nickname: "",
    name: "",
    birthDate: "",
    email: "",
    phone: "",
    address: "",
    detaileAdd: "",
    zonecode: "",
  });

  // 세션 데이터 가져오기
  useEffect(() => {
    const fetchSessionData = async () => {
      try {
        const response = await axios.get("/api/usr/oauth/get-oauth-session");
        const data = response.data;

        console.log("Fetched Session Data:", data);

        setFormData((prev) => ({
          ...prev,
          nickname: data.nickname || "",
          name: data.name || "",
          email: data.email || "",
        }));
      } catch (error) {
        console.error("세션 데이터 로드 실패:", error);
        // 실패 시 로그인 페이지로 리다이렉트
        navigate("/login");
      }
    };

    fetchSessionData();
  }, [navigate]);

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

  const autoHyphenPhone = (phone) => {
    return phone
      .replace(/[^0-9]/g, "")
      .replace(/^(\d{3})(\d{3,4})(\d{4})$/, "$1-$2-$3")
      .replace(/(\-{1,2})$/g, "");
  };

  const validateDuplicatesBeforeSubmit = async () => {
    // 추가된 부분: 제출 전에 아이디와 닉네임 중복 검사를 강제 실행
    if (!isUserIdChecked) {
      await handleDuplicateCheck("userId");
    }
    if (!isNicknameChecked) {
      await handleDuplicateCheck("nickname");
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("Submitting data:", formData); // 전달 데이터 출력
    await validateDuplicatesBeforeSubmit(); // 추가된 부분: 제출 전 중복 확인 실행

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

    try {
      const response = await postSocialSignupApi(formData);
      if (response.status === 200) {
        setCallback(() => () => navigate("/")); // Callback 상태로 분리
        setAlertModalConfig({
          isOpen: true,
          message: response.data.message,
        });
      }
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "회원가입 처리 중 문제가 발생했습니다.";
      setAlertModalConfig({
        isOpen: true,
        message: errorMessage,
      });
    }
  };

  const handleCancel = () => {
    navigate("/");
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
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
            placeholder="이메일"
            value={formData.email}
            readOnly
          />
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

export default SocialSignupComponent;
