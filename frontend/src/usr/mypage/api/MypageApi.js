import axios from "axios";

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.withCredentials = true; // 쿠키사용여부 설정

// 유저조회(존재유무)
export const getExistsUserApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/mypage/exists`, config);
  return res.data;
};

// 마이페이지 사용자정보
export const getMypageUserApi = async (param) => {
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

// 팔로우, 팔로잉 숫자
export const getFollowCountAllApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/follow/count/all`, config);
  return res.data;
};

// 팔로우상태
export const getFollowStatusApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/follow/status`, config);
  return res.data;
};

// 팔로우 추가,해제
export const followStatusApi = async (param, status) => {
  const config = {
    params: { ...param },
  };
  if (status) {
    const res = await axios.post(`/api/usr/follow`, null, config);
    return res.data;
  } else {
    const res = await axios.delete(`/api/usr/follow`, config);
    return res.data;
  }
};

// 프로필 정보
export const getProfileApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/profile`, config);
  return res.data;
};
