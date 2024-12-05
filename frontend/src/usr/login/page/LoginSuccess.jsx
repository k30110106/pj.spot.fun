import { useNavigate } from "react-router-dom";
import { useBasic } from "../../../common/context/BasicContext";
import { useEffect } from "react";
import axios from "axios";

const LoginSuccess = () => {
  const { setUserInfo } = useBasic();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axios.post("/api/usr/login/token/check");
        if (response.status === 200 && response.data) {
          alert(`${response.data}님, 환영합니다!`);
          setUserInfo(response.data); // 사용자 정보 업데이트
          setUserInfo(response.data.userId);
          setUserInfo(response.data.nickname);
          navigate("/"); // 메인 페이지로 리다이렉트
        } else {
          alert("사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요.");
          navigate("/login"); // 로그인 페이지로 이동
        }
      } catch (error) {
        console.error("Error fetching user info after OAuth login:", error);
        alert("로그인 중 문제가 발생했습니다. 다시 시도해주세요.");
        navigate("/login");
      }
    };

    fetchUserInfo();
  }, [setUserInfo, navigate]);

  return <div>로그인 처리 중입니다...</div>;
};

export default LoginSuccess;
