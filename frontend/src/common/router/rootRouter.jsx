import { Suspense, lazy } from "react";
import feedRouter from "../../usr/feed/router/feedRouter";

const { createBrowserRouter } = require("react-router-dom");

const Loading = <div>Loading....</div>;

const MainPage = lazy(() => import("../../usr/main/page/MainPage"));
const LoginPage = lazy(() => import("../../usr/login/page/LoginPage"));
const MyPage = lazy(() => import("../../usr/mypage/page/MyPage"));
const SignupPage = lazy(() => import("../../usr/signup/page/SignupPage"));
const FeedIndexPage = lazy(() => import("../../usr/feed/page/IndexPage"));
const CoursePage = lazy(() => import("../../usr/course/page/CourseListPage"));
// const AddDatePage = lazy(() => import("../../usr/course/page/AddDatePage")); // 수정된 부분

const rootRouter = createBrowserRouter(
  [
    {
      path: "",
      element: (
        <Suspense fallback={Loading}>
          <MainPage />
        </Suspense>
      ),
    },
    {
      path: "login",
      element: (
        <Suspense fallback={Loading}>
          <LoginPage />
        </Suspense>
      ),
    },
    {
      path: "mypage",
      element: (
        <Suspense fallback={Loading}>
          <MyPage />
        </Suspense>
      ),
    },
    {
      path: "Signup",
      element: (
        <Suspense fallback={Loading}>
          <SignupPage />
        </Suspense>
      ),
    },
    {
      path: "feed",
      element: (
        <Suspense fallback={Loading}>
          <FeedIndexPage />
        </Suspense>
      ),
      children: feedRouter(),
    },
    {
      path: "datecourses",
      element: (

        <Suspense fallback={Loading}>
          <CoursePage />
        </Suspense>

      ),
    },
    // {
    //   path: "addDate",
    //   element: (
    //     <Suspense fallback={Loading}>
    //       <AddDatePage /> {/* 수정된 부분 */}
    //     </Suspense>
    //   ),
    // },
  ],
  // {
  //   future: {
  //     v7_fetcherPersist: true,
  //     v7_normalizeFormMethod: true,
  //     v7_partialHydration: true,
  //     v7_skipActionErrorRevalidation: true,
  //     v7_relativeSplatPath: true,
  //   },
  // }
);

export default rootRouter;
