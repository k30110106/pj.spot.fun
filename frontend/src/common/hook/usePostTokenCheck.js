import { useEffect } from "react";
import { useBasic } from "../context/BasicContext"; // context에서 userIdx와 setUserIdx를 사용
import axios from "axios";

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.withCredentials = true; // 쿠키사용여부 설정

// 커스텀 훅: usePostTokenCheck
const usePostTokenCheck = () => {
  const { setUserInfo, setTokenLoading } = useBasic(); // Context에서 setUserIdx를 가져옴

  useEffect(() => {
    // postTokenCheck 함수 호출
    const postTokenCheck = async () => {
      setTokenLoading(true);

      try {
        const res = await axios.post("/api/usr/login/token/check");
        console.log("postTokenCheck userIdx >> " + res.data?.userIdx);
        // setUserInfo(res.data); // 상태 업데이트
        // setNickname(res.data.nickname); // 닉네임 업데이트
        if (res.status === 200 && res.data) {
          setUserInfo(res.data); // 토큰이 유효한 경우 사용자 정보 설정
        } else {
          setUserInfo(null); // 토큰이 없거나 유효하지 않은 경우 초기화
        }
      } catch (err) {
        if (err.response?.status === 401) {
          console.warn("Unauthorized: Token is invalid or expired.");
          setUserInfo(null); // 401 응답 시 상태 초기화
        } else {
          console.error("Error during token check:", err);
          setUserInfo(null); // 다른 에러 발생 시도 초기화
        }
      } finally {
        setTokenLoading(false);
      }
    };

    postTokenCheck();
  }, [setUserInfo]); // 의존성 배열에 setUserIdx를 넣어주어 업데이트가 필요할 때만 호출
};

export default usePostTokenCheck;
