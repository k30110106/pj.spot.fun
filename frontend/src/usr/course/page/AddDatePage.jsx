{/* 데이트 코스 추가 { 회원일때만 가능 }*/}
import React from 'react';
import BasicLayout from "../../../common/layout/BasicLayout";
import FixedDateCourseList from "../components/FixedDateCourseList";
import { AuthProvider, useAuth } from '../components/AuthContext';
import AddDateCourse from '../components/AddDateCourse';

const AddDatePageContent = () => {
    const { user, loading } = useAuth();

    if (loading) {
        // 로딩 중일 때 보여줄 UI (예: 로딩 스피너)
        return <p>Loading...</p>;
    }

    return (
        <div id='addDate-page'>
            <h1>데이트 코스 추가</h1>
            {/* {user ? (
                <FixedDateCourseList />
            ) : (
                <p>로그인 후 코스를 추가할 수 있습니다.</p>
            )} */}
        </div>
    );
};

const AddDatePage = () => {
    return (
        <AuthProvider>
            <BasicLayout>
                <AddDatePageContent />
                <AddDateCourse />
            </BasicLayout>
        </AuthProvider>
    );
};

export default AddDatePage;