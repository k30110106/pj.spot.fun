import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Feed Page Loading....</div>;
const ListPage = lazy(() => import("../page/ListPage"));

const feedRouter = () => {
  return [
    {
      path: "list",
      element: (
        <Suspense fallback={Loading}>
          <ListPage />
        </Suspense>
      ),
    },
    {
      path: "",
      element: <Navigate replace to="/feed/list" />,
    },
  ];
};

export default feedRouter;
