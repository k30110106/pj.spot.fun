import { lazy, useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useBasic } from "../../../common/context/BasicContext";
import { getExistUserApi } from "../api/MypageApi";

const FeedListCompoent = lazy(() => import("../component/feed/ListComponent"));
const LikeListCompoent = lazy(() => import("../component/like/ListComponent"));
const WishListCompoent = lazy(() => import("../component/wish/ListComponent"));

const ValidateComponent = () => {
  const navigate = useNavigate();
  const { userIdx } = useParams();
  const { userInfo } = useBasic();
  const { pathname } = useLocation();
  const loginUserIdx = userInfo?.userIdx || "";
  const [isDone, setIsDone] = useState(false);

  const existsUser = async () => {
    try {
      const data = await getExistUserApi({ idx: userIdx });
      return data;
    } catch (err) {
      console.log(err);
      return false;
    }
  };

  const renderComponent = () => {
    if (!isDone) {
      return;
    }

    // 본인이 아닌경우 feed페이지만 접근가능 처리
    if (pathname.includes("like")) {
      return <LikeListCompoent />;
    } else if (pathname.includes("wish")) {
      return <WishListCompoent />;
    }

    return <FeedListCompoent />;
  };

  useEffect(() => {
    const doVailidate = async () => {
      // 메인으로 이동
      if (loginUserIdx === "") {
        navigate("/", { replace: true });
        return;
      }

      // 마이페이지로 이동
      if (isNaN(userIdx) || !(await existsUser())) {
        navigate(`/mypage/feed/${loginUserIdx}`, { replace: true });
        return;
      }

      // feed 외 다른페이지는 본인만 접근가능
      if (
        !pathname.includes("feed") &&
        parseInt(loginUserIdx) !== parseInt(userIdx)
      ) {
        navigate(`/mypage/feed/${userIdx}`, { replace: true });
        return;
      }

      setIsDone(true);
    };

    doVailidate();
  }, [loginUserIdx, userIdx, pathname]);

  return <>{renderComponent()}</>;
};

export default ValidateComponent;
