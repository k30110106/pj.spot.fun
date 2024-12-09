import Footer from "../../../common/component/Footer";
import Header from "../../../common/component/Header";
import SignupComponent from "../component/SignupComponent";

const SignupPage = () => {
  return (
    <div className="custom-cursor">
      <div id="Singup-page">
        <Header />
        <h1 className="flex flex-col justify-center items-center text-2xl font-bold m-10">회원가입</h1>
        <SignupComponent />
        <Footer />
      </div>
    </div>
  );
};

export default SignupPage;
