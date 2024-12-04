import { Outlet } from "react-router-dom";
import usePostTokenCheck from "../../../common/hook/usePostTokenCheck";

const IndexPage = () => {
  usePostTokenCheck();

  return (
    <div className="border border-gray-200 w-full">
      피드 인덱스 페이지
      <div className="border border-red-200 flex w-full">
        <Outlet />
      </div>
    </div>
  );
};

export default IndexPage;
