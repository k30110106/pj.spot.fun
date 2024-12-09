import React from "react";
import Header from "../../../common/component/Header";
import Footer from "../../../common/component/Footer";
import SocialSignupComponent from "../component/SocialSignupComponent";

function SocialSignupPage() {
  return (
    <div className="custom-cursor">
      <div id="Singup-page">
        <Header />
        <h1 className="flex flex-col justify-center items-center text-2xl font-bold m-10">회원가입</h1>
        <SocialSignupComponent />
        <Footer />
      </div>
    </div>
  );
}

export default SocialSignupPage;
