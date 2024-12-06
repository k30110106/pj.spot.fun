import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading...</div>;
const AddCoursePage = lazy(() => import("../page/AddCoursePage"));

const DateRouter = () => {
  return [
    {
      path: "add-course", // '/date/add-course' 경로
      element: (
        <Suspense fallback={Loading}>
          <AddCoursePage />
        </Suspense>
      ),
    },
    {
      path: "", // 기본 경로에서 리다이렉트
      element: <Navigate replace to="/date/addcourse" />,
    },
  ];
};

export default DateRouter;
