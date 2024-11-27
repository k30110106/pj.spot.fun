import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { postSignupApi } from "../api/SignupApi";
import AddressModal from "../../../common/AddressModal";
import AlertModal from "../../../common/AlertModal";

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
    const [isTouched, setIsTouched] = useState({}); // 각 필드의 터치 여부 추적
    const [addressModalOpen, setAddressModalOpen] = useState(false); // 주소 모달 상태
    const [alertModalConfig, setAlertModalConfig] = useState({ isOpen: false, message: "", callback: null }); // 알림 모달 상태
    const navigate = useNavigate();

    // 유효성 검사 로직
    useEffect(() => {
        const newErrors = {};

        if (isTouched.userId && !formData.userId.match(/^[a-zA-Z0-9]{4,12}$/)) {
            newErrors.userId = "아이디는 영문, 숫자로 4~12자 사이여야 합니다.";
        }

        if (isTouched.name && !formData.name.match(/^[a-zA-Z가-힣]+$/)) {
            newErrors.name = "이름은 한글 또는 영문만 가능합니다.";
        }

        if (isTouched.nickname && !formData.nickname.match(/^[a-zA-Z0-9가-힣]{1,12}$/)) {
            newErrors.nickname = "닉네임은 한글, 영문, 숫자로 1~12자 사이여야 합니다.";
        }

        const phoneRegex = /^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$/;
        if (isTouched.phone && !phoneRegex.test(formData.phone)) {
            newErrors.phone = "유효한 핸드폰 번호를 입력해주세요.";
        }

        if (isTouched.email && !formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            newErrors.email = "유효한 이메일 형식이 아닙니다.";
        }

        if (
            isTouched.password &&
            !formData.password.match(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/)
        ) {
            newErrors.password = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~16자여야 합니다.";
        }

        if (isTouched.confirmPassword && formData.password !== formData.confirmPassword) {
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
//         // 실패한 필드 목록
//             const errorFields = Object.keys(newErrors);
//
//             if (errorFields.length > 0) {
//                 // 실패한 필드가 하나일 경우 해당 필드에 포커스
//                 if (errorFields.length === 1) {
//                     document.getElementsByName(errorFields[0])[0]?.focus();
//                 }
//                 // 실패한 필드가 여러 개일 경우 첫 번째 필드에 포커스
//                 else {
//                     document.getElementsByName(errorFields[0])[0]?.focus();
//                 }
//             }
    }, [formData, isTouched]); // formData 또는 isTouched가 변경될 때 실행

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "userId") {
            const filteredValue = value.replace(/[^a-zA-Z0-9]/g, ""); // 영문, 숫자만 허용
            setFormData({ ...formData, [name]: filteredValue });
        } else if (name === "phone") {
            const formattedPhone = autoHyphenPhone(value); // 자동 하이픈 추가
            setFormData({ ...formData, [name]: formattedPhone });
        } else {
            setFormData({ ...formData, [name]: value });
        }

        // 필드를 터치한 상태로 변경
        setIsTouched((prev) => ({ ...prev, [name]: true }));
    };

    const autoHyphenPhone = (phone) => {
        return phone
            .replace(/[^0-9]/g, "") // 숫자만 남김
            .replace(/^(\d{3})(\d{3,4})(\d{4})$/, "$1-$2-$3") // 하이픈 추가 (중간 3~4자리 허용)
            .replace(/(\-{1,2})$/g, ""); // 불필요한 하이픈 제거
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
const errorFields = Object.keys(errors).filter((key) => errors[key]);
    if (errorFields.length > 0) {
        const firstErrorField = errorFields[0];
        document.getElementsByName(firstErrorField)[0]?.focus();
        return;
    }

    try {
        const response = await postSignupApi(formData);
        if (response.status === 200) {
            setAlertModalConfig({
                isOpen: true,
                message: response.data.message,
                callback: () => navigate("/login"),
            });
        } else {
            setAlertModalConfig({
                isOpen: true,
                message: response.data.message || "회원가입에 실패했습니다.",
                callback: null,
            });
        }
    } catch (error) {
        console.error("Signup error:", error);
        setAlertModalConfig({
            isOpen: true,
            message: "서버와 통신 중 문제가 발생했습니다.",
            callback: null,
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
                    {errors.confirmPassword && <p style={{ color: "red" }}>{errors.confirmPassword}</p>}
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
                    {errors.birthDate && <p style={{ color: "red" }}>{errors.birthDate}</p>}
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
                    {errors.nickname && <p style={{ color: "red" }}>{errors.nickname}</p>}
                </div>

                <div>
                    <label>핸드폰: </label>
                    <input
                        type="text"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        maxLength="13" // 하이픈 포함 최대 길이 제한
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
                    />
                    {errors.email && <p style={{ color: "red" }}>{errors.email}</p>}
                </div>

                <div>
                    <label>우편번호: </label>
                    <div>
                        <input type="text" name="zonecode" value={formData.zonecode} readOnly />
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
             isOpen={addressModalOpen} // 주소 모달 상태
             onClose={() => setAddressModalOpen(false)} // 모달 닫기
             onComplete={(data) => {
                 setFormData({ ...formData, address: data.address, zonecode: data.zonecode }); // 주소 입력
                 setAddressModalOpen(false); // 모달 닫기
             }}
         />

         <AlertModal
             isOpen={alertModalConfig.isOpen} // 알림 모달 상태
             message={alertModalConfig.message} // 알림 메시지
             onClose={() => {
                 setAlertModalConfig({ isOpen: false, message: "", callback: null }); // 모달 닫기
                 if (alertModalConfig.callback) alertModalConfig.callback(); // 콜백 실행
             }}
         />
        </div>
    );
};

export default SignupComponent;
