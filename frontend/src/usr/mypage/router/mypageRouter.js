import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>MyPage Page Loading....</div>;
const FeedListCompoent = lazy(() => import("../component/feed/ListComponent"));
const LikeListCompoent = lazy(() => import("../component/like/ListComponent"));
const WishListCompoent = lazy(() => import("../component/wish/ListComponent"));

const mypageRouter = () => {
  return [
    {
      path: "feed/:userIdx",
      element: (
        <Suspense fallback={Loading}>
          <FeedListCompoent />
        </Suspense>
      ),
    },
    {
      path: "like/:userIdx",
      element: (
        <Suspense fallback={Loading}>
          <LikeListCompoent />
        </Suspense>
      ),
    },
    {
      path: "wish/:userIdx",
      element: (
        <Suspense fallback={Loading}>
          <WishListCompoent />
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
