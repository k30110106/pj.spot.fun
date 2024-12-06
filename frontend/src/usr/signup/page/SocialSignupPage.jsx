import React from "react";
import Header from "../../../common/component/Header";
import Footer from "../../../common/component/Footer";
import SocialSignupComponent from "../component/SocialSignupComponent";

function SocialSignupPage() {
  return (
    <div id="Singup-page">
      <Header />
      회원가입
      <SocialSignupComponent />
      <Footer />
    </div>
  );
}

export default SocialSignupPage;
