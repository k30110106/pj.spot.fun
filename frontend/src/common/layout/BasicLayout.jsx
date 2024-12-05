import React from 'react';
import Header from "../component/Header";
import Footer from "../component/Footer";
// import Aside from "../component/Aside";
// import NavigationMenu from '../../usr/main/component/NavigationMenu';
// import RecommendedCourses from '../../usr/main/component/RecommendedCourses';
// import PopularCourses from '../../usr/main/component/PopularCourses';
// import NewUserCourses from '../../usr/main/component/NewUserCourses';
// import Banner from '../../usr/main/component/Banner';
import usePostTokenCheck from "../hook/usePostTokenCheck";
import MainPage from '../../usr/main/page/MainPage';


const BasicLayout = ({ children }) => {
    usePostTokenCheck();
    return (
        <div>
            <Header />
            <main id="basic-content">
                {children}
            </main>
            {/* <MainPage /> */}
            {/* <Aside /> */}
            <Footer />
        </div>
    );
}

export default BasicLayout;