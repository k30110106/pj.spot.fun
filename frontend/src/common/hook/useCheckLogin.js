import axios from "axios";

/***
 * 로그인 상태여부 커스텀 훅 (수정중)
 * >> 토큰검증 + 로그인상태를 체크해서 true, false를 반환합니다
 *
 * 1. 엑세스 토큰 조회 (postAccessTokenApi)
 *  1-1. 정상 -> TRUE
 *  1-2. 실패 -> 리프레시 토큰 조회 + 엑세스 토큰 재발급 시도 (postRefreshTokenApi)
 *      1-2-1. 정상 -> TRUE
 *      1-2-2. 실패 -> FALSE
 */
const API_BASE_URL = process.env.REACT_APP_API_ROOT;

const useCheckLogin = () => {
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
        if (res.status === 200) {
          return true;
        } else {
          return false;
        }
      })
      .catch((err) => {
        return false;
      });
  };

  const checkLogin = async () => {
    try {
      const response = await postAccessTokenApi();
      if (response.status === 200) {
        return true;
      } else {
        return await postRefreshTokenApi();
      }
    } catch (e) {
      console.error(e);
      return await postRefreshTokenApi();
    }
  };

  return { checkLogin };
};

export default useCheckLogin;
