import axios from "axios";

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;

export const getFeedListApi = async () => {
  const res = await axios.get(`${API_BASE_URL}/api/usr/feed`);
  return res.data;
};

export const getFeedDetailApi = async (idx) => {
  return await axios.get(`${API_BASE_URL}/api/usr/feed${idx}`);
};

export const postFeedInsertApi = async () => {
  return await axios.post(`${API_BASE_URL}/api/usr/feed`);
};
