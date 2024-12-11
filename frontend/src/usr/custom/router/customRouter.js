import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Page Loading....</div>;
const ListPage = lazy(() => import("../page/ListPage"));
const AddPage = lazy(() => import("../page/AddPage"));
const ReadPage = lazy(() => import("../page/ReadPage"));
const UpdatePage = lazy(() => import("../page/UpdatePage"));

const customRouter = () => {
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
      element: <Navigate replace to="custom/list" />,
    },
    {
      path: "add",
      element: (
        <Suspense fallback={Loading}>
          <AddPage />
        </Suspense>
      ),
    },
    {
      path: "read/:cno",
      element: (
        <Suspense fallback={Loading}>
          <ReadPage />
        </Suspense>
      ),
    },
    {
      path: "update/:cno",
      element: (
        <Suspense fallback={Loading}>
          <UpdatePage />
        </Suspense>
      ),
    },
  ];
};

export default customRouter;
