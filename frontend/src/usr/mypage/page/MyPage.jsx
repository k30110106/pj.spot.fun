import React, { useEffect } from "react";
import IndexComponent from "../component/IndexComponent";
import MenuComponent from "../component/MenuComponent";
import BasicLayout from "../../../common/layout/BasicLayout";

const MyPage = () => {
  return (
    <BasicLayout>
      <div id="main-page">
        마이페이지
        <div>
          <IndexComponent />
        </div>
        <div>
          <MenuComponent />
        </div>
      </div>
    </BasicLayout>
  );
};

export default MyPage;
