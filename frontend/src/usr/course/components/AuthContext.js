// 사용자확인
import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

// AuthContext 생성
const AuthContext = createContext();

// useAuth 훅
export const useAuth = () => {
  return useContext(AuthContext);
};

// AuthProvider 컴포넌트
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 컴포넌트가 마운트될 때 토큰을 검증
    const token = localStorage.getItem('authToken');

    if (token) {
      // 서버에서 토큰 검증
      axios.get('/api/usr/course/datecourses', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
        .then(response => {
          setUser(response.data);  // 서버에서 반환된 유저 데이터
          setLoading(false);
        })
        .catch(error => {
          console.error("Token verification failed", error);
          setUser(null);  // 유효하지 않으면 user를 null로 설정
          setLoading(false);
        });
    } else {
      setLoading(false); // 토큰이 없다면 loading을 끝냄
    }
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
export default AuthProvider;