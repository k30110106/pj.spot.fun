import axios from "axios";

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;

export const searchPlaces = async (address) => {
  // 주소로 장소 검색
  const response = await axios.get(`${API_BASE_URL}/api/usr/place/search`, {
    params: { address },
  });
  return response.data;
};

export const registerDateCourse = async (course) => {
  // 데이트 코스 등록
  const response = await axios.post(`${API_BASE_URL}/api/usr/custom/`, course);
  return response.data;
};

export const getCustomList = async () => {
  const response = await axios.get(`${API_BASE_URL}/api/usr/custom/list`);
  return response.data;
};

export const getCustomDetail = async (cno) => {
  const response = await axios.get(`${API_BASE_URL}/api/usr/custom/${cno}`);
  return response.data;
};

export const updateCustom = async (cno, customObj) => {
  const response = await axios.put(
    `${API_BASE_URL}/api/usr/custom/${cno}`,
    customObj
  );
  return response.data;
};

export const deleteCustom = async (cno) => {
  const response = await axios.delete(`${API_BASE_URL}/api/usr/custom/${cno}`);
  return response.data;
};
