import axios from "axios";

const API_BASE_URL = process.env.REACT_APP_API_ROOT;

export const postSignupApi = async (param) => {
    try {
        const res = await axios.post(`${API_BASE_URL}/api/usr/signup/signup`, param, {
            headers: {
                "Content-Type": "application/json",
            },
        });
        return { status: res.status, data: res.data };
    } catch (err) {
        console.error("API error:", err);
        const status = err.response ? err.response.status : 500;
        return { status, data: err.response ? err.response.data : "Unknown error" };
    }
};

export const checkDuplicateApi = (data) => {
    return axios.post(`${API_BASE_URL}/api/usr/signup/check-duplicate`, data);
};