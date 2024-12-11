import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading...</div>;
const AddCoursePage = lazy(() => import("../page/AddCoursePage"));

const DateRouter = () => {
  return [
    {
      path: "addcourse", // 상대 경로로 설정되도록
      element: (
        <Suspense fallback={Loading}>
          <AddCoursePage />
        </Suspense>
      ),
    },
    {
      path: "", // /datecourses 기본 경로에서 리다이렉트
      element: <Navigate replace to="addcourse" />, // /datecourses -> /datecourses/addcourse 로 리다이렉트
    },
  ];
};


export default DateRouter;
