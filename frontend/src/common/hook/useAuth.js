// hooks/useAuth.js를 수정하여 기존 토큰 체크 로직 활용
import usePostTokenCheck from './usePostTokenCheck';
import useCheckLogin from './useCheckLogin';
import { useBasic } from '../context/BasicContext';

export const useAuth = () => {
    const { userInfo } = useBasic();
    const { checkLogin } = useCheckLogin();
    usePostTokenCheck(); // 토큰 체크 훅 사용

    return {
        isAuthenticated: !!userInfo,
        user: userInfo,
        checkLogin
    };
};