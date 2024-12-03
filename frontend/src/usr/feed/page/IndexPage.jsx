import React from "react";
import BasicLayout from "../../../common/layout/BasicLayout";
import { Outlet } from "react-router-dom";

const IndexPage = () => {
  return (
    <BasicLayout>
      <div id="feed-index-page" style={{ border: "1px solid black" }}>
        피드 인덱스 페이지
        <div className="flex flex-wrap w-full ">
          <Outlet />
        </div>
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
