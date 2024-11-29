import axios from "axios";
import { useNavigate } from "react-router-dom";

/**
 * 토큰 조회 커스텀 훅 (수정중)
 * >> 비로그인 상태에서 접근 시 강제로 로그인페이지 이동처리
 *
 * 1. 엑세스 토큰 조회 (postAccessTokenApi)
 *  1-1. 정상 -> PASS
 *  1-2. 실패 -> 리프레시 토큰 조회 + 엑세스 토큰 재발급 시도 (postRefreshTokenApi)
 *      1-2-1. 정상 -> PASS
 *      1-2-2. 실패 -> 로그인페이지 이동
 * */
const API_BASE_URL = process.env.REACT_APP_API_ROOT;

export const useCheckToken = () => {
  const navigate = useNavigate();

  const postAccessTokenApi = async () => {
    const accessToken = localStorage.getItem("access_token");
    const config = {
      headers: { Authorization: `Bearer ${accessToken}` },
    };

    return await axios.post(`${API_BASE_URL}/api/token/access`, null, config);
  };

  const postRefreshTokenApi = async () => {
    const config = {
      withCredentials: true,
    };

    await axios
      .post(`${API_BASE_URL}/api/token/refresh`, null, config)
      .then((res) => {
        //console.log(res);
        if (res.status !== 200) {
          window.alert("토큰 재발급 실패! 로그인페이지 이동!");
          navigate({ pathname: "/login" }, { replace: true });
          return;
        }
        console.log("토큰 재발급 성공");
        localStorage.setItem("access_token", res.data.accessToken);
      })
      .catch((err) => {
        //console.log(err);
        window.alert("토큰 재발급 실패! 로그인페이지 이동!");
        navigate({ pathname: "/login" }, { replace: true });
      });
  };

  const checkToken = async () => {
    try {
      const response = await postAccessTokenApi();
      if (response.status !== 200) {
        await postRefreshTokenApi();
        return;
      }
      console.log("토큰 인증 성공");
    } catch (e) {
      console.error(e);
      await postRefreshTokenApi();
    }
  };

  return { checkToken };
};
