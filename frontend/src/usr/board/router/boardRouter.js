import { lazy, Suspense } from "react";

const Loading = <div>Loading....</div>;
const BoardListPage = lazy(() => import("../page/BoardListPage"));
const BoardDetailPage = lazy(() => import("../page/BoardDetailPage"));
const BoardCreatePage = lazy(() => import("../page/BoardCreatePage"));

const boardRouter = () => [
  {
    path: "boardlist", // 상대 경로로 수정
    element: (
      <Suspense fallback={Loading}>
        <BoardListPage />
      </Suspense>
    ),
  },
  {
    path: "board/:id", // 상대 경로로 수정
    element: (
      <Suspense fallback={Loading}>
        <BoardDetailPage />
      </Suspense>
    ),
  },
  {
    path: "boardcreate", // 상대 경로로 수정
    element: (
      <Suspense fallback={Loading}>
        <BoardCreatePage />
      </Suspense>
    ),
  },
];

export default boardRouter;
