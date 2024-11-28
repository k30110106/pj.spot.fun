import React from "react";

const Searchpw = ({ onClose }) => {
    const [formData, setFormData] = React.useState({
        name: '',
        birthDate: '',
        userId: '',
        email: '',
        verificationCode: '',
    });
    const [isEmailVerified, setIsEmailVerified] = React.useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSendVerification = () => {
        console.log("Sending verification email to:", formData.email);
    };

    const handleVerifyCode = () => {
        console.log("Verifying code:", formData.verificationCode);
        setIsEmailVerified(true); // 인증 성공 가정
    };

    const handleSubmit = () => {
        if (isEmailVerified) {
            console.log("Recovering Password with:", formData);
        } else {
            alert("이메일 인증을 완료해주세요.");
        }
    };

    return (
        <div>
            <h2>비밀번호 찾기</h2>
            <input type="text" name="name" placeholder="이름" onChange={handleInputChange} />
            <input type="date" name="birthDate" placeholder="생년월일" onChange={handleInputChange} />
            <input type="text" name="userId" placeholder="아이디" onChange={handleInputChange} />
            <input type="email" name="email" placeholder="이메일" onChange={handleInputChange} />
            <button onClick={handleSendVerification}>인증 메일 보내기</button>
            <input
                type="text"
                name="verificationCode"
                placeholder="인증 코드 입력"
                onChange={handleInputChange}
            />
            <button onClick={handleVerifyCode}>코드 확인</button>
            <button onClick={handleSubmit}>비밀번호 찾기</button>
            <button onClick={onClose}>닫기</button>
        </div>
    );
};

export default Searchpw;