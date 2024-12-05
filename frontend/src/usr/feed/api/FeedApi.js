import axios from "axios";

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.withCredentials = true; // 쿠키사용여부 설정

// 목록조회
export const getFeedListApi = async (param) => {
  const config = {
    params: param,
  };
  const res = await axios.get(`/api/usr/feed`, config);
  return res.data;
};

// 상세조회
export const getFeedDetailApi = async (idx) => {
  const res = await axios.get(`/api/usr/feed/${idx}`);
  return res.data;
};

// 신규등록
export const postFeedInsertApi = async (form) => {
  const header = {
    header: {
      "Content-Type": "multipart/form-data",
    },
  };
  const res = await axios.post(`/api/usr/feed`, form, header);
  return res.data;
};

// 댓글목록 조회
export const getFeedCommentListApi = async (idx) => {
  const res = await axios.get(`/api/usr/feed/comment/${idx}`);
  return res.data;
};

// 좋아요 등록, 삭제
export const feedLikeApi = async (param) => {
  const { idx, likedYn } = param;

  if (likedYn) {
    const res = await axios.post(`/api/usr/feed/like/${idx}`);
    return res.data;
  } else {
    const res = await axios.delete(`/api/usr/feed/like/${idx}`);
    return res.data;
  }
};

// 댓글 작성
export const postCommentApi = async (param) => {
  const { idx, content } = param;
  const config = {
    params: { content },
  };

  const res = await axios.post(`/api/usr/feed/comment/${idx}`, null, config);
  return res.data;
};

// 해시태그 목록
export const getHashtagApi = async () => {
  const res = await axios.get(`/api/usr/feed/hashtag`);
  return res.data;
};

export const postCommentReplyApi = async (param) => {
  const config = {
    params: { ...param },
  };
  const res = await axios.post(`/api/usr/feed/comment/reply`, null, config);
  return res.data;
};
