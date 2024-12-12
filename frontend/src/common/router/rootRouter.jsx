import { Suspense, lazy } from "react";
import feedRouter from "../../usr/feed/router/feedRouter";
import boardRouter from "../../usr/board/router/boardRouter";
import BasicLayout from "../layout/BasicLayout";
import dateRouter from "../../usr/course/router/DateRouter"; // DateRouter 추가
import mypageRouter from "../../usr/mypage/router/mypageRouter";
import customRouter from "../../usr/custom/router/customRouter";

const { createBrowserRouter } = require("react-router-dom");

const Loading = <div>Loading....</div>;

const MainPage = lazy(() => import("../../usr/main/page/MainPage"));
const LoginPage = lazy(() => import("../../usr/login/page/LoginPage"));
const LoginSuccessPage = lazy(() =>
  import("../../usr/login/page/LoginSuccess")
);
const MyPageIndexPage = lazy(() => import("../../usr/mypage/page/IndexPage"));
const SignupPage = lazy(() => import("../../usr/signup/page/SignupPage"));
const FeedIndexPage = lazy(() => import("../../usr/feed/page/IndexPage"));
// const CoursePage = lazy(() => import("../../usr/course/page/CourseListpage"));
const BoardIndexPage = lazy(() =>
  import("../../usr/board/page/BoardIndexPage")
);
const SocialSignupPage = lazy(() =>
  import("../../usr/signup/page/SocialSignupPage")
);
const DateCourseListPage = lazy(() =>
  import("../../usr/course/page/DateCourseListpage")
);
const AddCoursePage = lazy(() => import("../../usr/course/page/AddCoursePage")); // 수정된 부분
const SignupCheckPage = lazy(() =>
  import("../../usr/signup/page/SignupCheckPage")
);
const CustomIndexPage = lazy(() => import("../../usr/custom/page/IndexPage"));

const rootRouter = createBrowserRouter(
  [
    {
      path: "",
      element: (
        // BasicLayout이 Suspense의 영향을 받아 무시되므로 Mainpage만 BasicLayout으로 적용
        <BasicLayout>
          <Suspense fallback={Loading}>
            <MainPage />
          </Suspense>
        </BasicLayout>
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
      path: "login-success",
      element: (
        <Suspense fallback={Loading}>
          <LoginSuccessPage />
        </Suspense>
      ),
    },
    {
      path: "social-signup",
      element: (
        <Suspense fallback={Loading}>
          <SocialSignupPage />
        </Suspense>
      ),
    },
    {
      path: "mypage",
      element: (
        <Suspense fallback={Loading}>
          <MyPageIndexPage />
        </Suspense>
      ),
      children: mypageRouter(),
    },
    {
      path: "signupCheck",
      element: (
        <Suspense fallback={Loading}>
          <SignupCheckPage />
        </Suspense>
      ),
    },
    {
      path: "signup",
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
      path: "board",
      element: (
        <Suspense fallback={Loading}>
          <BoardIndexPage />
        </Suspense>
      ),
      children: boardRouter(),
    },
    {
      path: "datecourses", // 데이트 코스 목록 페이지 경로
      element: (
        <Suspense fallback={Loading}>
          <DateCourseListPage />
        </Suspense>
      ),
      children: dateRouter(), // DateRouter 자식 라우터 추가
    },
    {
      path: "addcourse",
      element: (
        <Suspense fallback={Loading}>
          <AddCoursePage />
        </Suspense>
      ),
    },

    {
      path: "custom",
      element: (
        <Suspense fallback={Loading}>
          <CustomIndexPage />
        </Suspense>
      ),
      children: customRouter(),
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
  {
    future: {
      v7_fetcherPersist: true,
      v7_normalizeFormMethod: true,
      v7_partialHydration: true,
      v7_skipActionErrorRevalidation: true,
      v7_relativeSplatPath: true,
    },
  }
);

export default rootRouter;
