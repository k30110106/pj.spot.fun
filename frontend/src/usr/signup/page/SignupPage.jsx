import Footer from "../../../common/component/Footer";
import Header from "../../../common/component/Header";
import BasicLayout from "../../../common/layout/BasicLayout";
import SignupComponent from "../component/SignupComponent";

const SignupPage = () => {
    return (
        <div id="Singup-page">
            <Header />
            회원가입
            <SignupComponent />
            <Footer />
        </div>

    );
};

export default SignupPage;