import BasicLayout from "../../../common/layout/BasicLayout";
import { Outlet } from "react-router-dom";

const IndexPage = () => {
  return (
    <BasicLayout>
      <div className="border border-gray-200 w-full">
        피드 인덱스 페이지
        <div className="border border-red-200 flex w-full">
          <Outlet />
        </div>
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
