import axios from "axios";

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.withCredentials = true; // 쿠키사용여부 설정

// 유저조회
export const getExistUserApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/mypage/user`, config);
  return res.data;
};

// 피드 목록조회
export const getFeedListApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/mypage/feed`, config);
  return res.data;
};

// 좋아요 목록조회
export const getLikeListApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/mypage/like`, config);
  return res.data;
};
