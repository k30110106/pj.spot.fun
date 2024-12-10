import { lazy, useEffect } from "react";
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
      // 파라미터(userIdx) 숫자여부 확인
      if (loginUserIdx === "" || isNaN(userIdx) || !(await existsUser())) {
        navigate("/", { replace: true });
      }

      // feed 외 다른페이지는 본인만 접근가능
      if (
        !pathname.includes("feed") &&
        parseInt(loginUserIdx) !== parseInt(userIdx)
      ) {
        navigate(`/mypage/feed/${userIdx}`, { replace: true });
      }
    };

    doVailidate();
  }, [loginUserIdx, userIdx, pathname]);

  return <>{renderComponent()}</>;
};

export default ValidateComponent;
