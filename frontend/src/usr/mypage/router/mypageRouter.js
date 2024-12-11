import { Suspense } from "react";
import { Navigate } from "react-router-dom";
import ValidateComponent from "../component/ValidateComponent";

const Loading = <div>MyPage Page Loading....</div>;

const mypageRouter = () => {
  return [
    {
      path: "feed/:userIdx",
      element: (
        <Suspense fallback={Loading}>
          <ValidateComponent />
        </Suspense>
      ),
    },
    {
      path: "like/:userIdx",
      element: (
        <Suspense fallback={Loading}>
          <ValidateComponent />
        </Suspense>
      ),
    },
    {
      path: "wish/:userIdx",
      element: (
        <Suspense fallback={Loading}>
          <ValidateComponent />
        </Suspense>
      ),
    },
    {
      path: "",
      element: <Navigate replace to="/mypage/feed/:userIdx" />,
    },
  ];
};

export default mypageRouter;
