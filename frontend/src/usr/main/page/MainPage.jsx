import BasicLayout from "../../../common/layout/BasicLayout";
import HelloComponent from "../component/HelloComponent";
import Banner from "../component/Banner";
import NavigationMenu from "../component/NavigationMenu";
import RecommendedCourses from "../component/RecommendedCourses";
import PopularCourses from "../component/PopularCourses";
import NewUserCourses from "../component/NewUserCourses";
import Header from "../../../common/component/Header";
import Footer from "../../../common/component/Footer";
import SideNavigationMenu from "../component/SideNavigationMenu";

const MainPage = () => {
  return (
    // <BasicLayout>
    <div className="custom-cursor">
      <div className="min-w-full pb-32">
        {/* <Header /> */}
        {/* <HelloComponent /> */}
        <Banner />
        <NavigationMenu />
        <RecommendedCourses />
        <PopularCourses />
        <NewUserCourses />
        {/* <Footer /> */}
      </div>
    </div>
    // </BasicLayout>
  );
};

export default MainPage;
