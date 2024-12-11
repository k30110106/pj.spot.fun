import BasicLayout from "../../../common/layout/BasicLayout";
import { Outlet } from "react-router-dom";

const IndexPage = () => {
  return (
    <BasicLayout>
      <div>
        커스텀 인덱스 페이지
        <div>
          <Outlet />
        </div>
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
