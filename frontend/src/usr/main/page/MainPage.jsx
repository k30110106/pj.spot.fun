import BasicLayout from "../../../common/layout/BasicLayout";
import HelloComponent from "../component/HelloComponent";
import Banner from "../component/Banner";
import NavigationMenu from "../component/NavigationMenu";
import RecommendedCourses from "../component/RecommendedCourses";
import PopularCourses from "../component/PopularCourses";
import NewUserCourses from "../component/NewUserCourses";
import Header from "../../../common/component/Header";
import Footer from "../../../common/component/Footer";

const MainPage = () => {
  return (
    // <BasicLayout>
    <div className="min-w-full">
      {/* <Header /> */}
      {/* <HelloComponent /> */}
      <Banner />
      <NavigationMenu />
      <RecommendedCourses />
      <PopularCourses />
      <NewUserCourses />
      {/* <Footer /> */}
    </div>
    // </BasicLayout>
  );
};

export default MainPage;
