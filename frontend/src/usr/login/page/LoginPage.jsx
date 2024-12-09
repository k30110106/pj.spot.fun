// import BasicLayout from "../../../common/layout/BasicLayout";
import Footer from "../../../common/component/Footer";
import Header from "../../../common/component/Header";
import LoginComponent from "../component/LoginComponent";
// import { useBasic } from "../../../common/context/BasicContext";
// import usePostTokenCheck from "../../../common/hook/usePostTokenCheck";

const LoginPage = () => {
  return (
    <div className="custom-cursor">
      <div id="login-page">
        <Header />
        {/* <div className="text-center font-bold text-3xl">로그인</div> */}
        <LoginComponent />
        <Footer />
      </div>
    </div>
  );
};

export default LoginPage;
