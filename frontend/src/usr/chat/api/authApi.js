// api/authApi.js
export const authApi = {
    // 현재 로그인한 사용자 정보 조회
    getCurrentUser: async () => {
        const response = await fetch('/api/user/current');
        return response.json();
    },

    // 로그인 처리
    login: async (credentials) => {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(credentials),
        });
        return response.json();
    }
};