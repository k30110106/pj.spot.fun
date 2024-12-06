import axios from "axios";

const API_BASE_URL = process.env.REACT_APP_API_ROOT;
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.withCredentials = true; // 쿠키사용여부 설정

export const postLoginApi = async (param) => {
  console.log("Login request payload:", param); // 요청 데이터 확인
  try {
    const res = await axios.post(`/api/usr/login/login`, param, {
      headers: {
        "Content-Type": "application/json", // JSON 요청 명시
      },
    });
    return { status: res.status, data: res.data }; // 성공 시 반환
  } catch (err) {
    console.log(err);
    return { status: err.response?.status || 500, data: null }; // 실패 시 반환
  }
};
