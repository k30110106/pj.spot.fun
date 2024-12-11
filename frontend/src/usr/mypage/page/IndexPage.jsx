import BasicLayout from "../../../common/layout/BasicLayout";
import { Outlet, useLocation, useNavigate, useParams } from "react-router-dom";
import ProfileComponent from "../component/ProfileComponent";
import ButtonComponent from "../component/ButtonComponent";
import { useEffect, useState } from "react";
import MenuTabComponent from "../component/MenuTabComponent";
import { useBasic } from "../../../common/context/BasicContext";
import {
  followStatusApi,
  getFollowCountAllApi,
  getFollowStatusApi,
  getMypageUserApi,
} from "../api/MypageApi";

const menuList = [
  {
    id: "feed",
    name: "피드",
  },
  {
    id: "like",
    name: "좋아요",
  },
  {
    id: "wish",
    name: "위시리스트",
  },
];
const initMenu = menuList[0].id;

const initCount = {
  followCount: 0,
  followingCount: 0,
};

const IndexPage = () => {
  const { userIdx } = useParams();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";
  const { pathname } = useLocation();

  // 탭 메뉴
  const navigate = useNavigate();
  const [activeMenu, setActiveMenu] = useState(initMenu);
  const handleMenuTabClickEvent = (menuId) => {
    navigate(`/mypage/${menuId}/${userIdx}`);
    setActiveMenu(menuId);
  };

  // 피드 개수
  const [feedCount, setFeedCount] = useState(0);
  const handleFeedCountEvent = (count) => {
    setFeedCount(count);
  };

  // 팔로우
  const [followStatus, setFollowStatus] = useState(false);
  const [followCount, setFollowCount] = useState(initCount);
  const handleFollowClickEvent = async () => {
    try {
      const status = !followStatus;
      const param = { followerIdx: loginUserIdx, followingIdx: userIdx };
      const data = await followStatusApi(param, status);

      setFollowStatus(data.followStatus);
      setFollowCount((prev) => {
        return {
          ...prev,
          followingCount: data.followStatus
            ? prev.followingCount + 1
            : prev.followingCount - 1,
        };
      });
    } catch (err) {
      console.log("[팔로우상태] 변경을 실패했습니다");
      console.log(err);
    }
  };

  // 사용자정보
  const [nickname, setNickname] = useState(null);

  useEffect(() => {
    const setNowMenuTab = () => {
      menuList.map(
        (menu) => pathname.includes(menu.id) && setActiveMenu(menu.id)
      );
    };
    const getFollowCount = async () => {
      try {
        const data = await getFollowCountAllApi({ userIdx });
        const { followerCount, followingCount } = data;
        setFollowCount({ followerCount, followingCount });
      } catch (err) {
        console.log("[팔로우수] 조회를 실패했습니다");
        console.log(err);
      }
    };
    const getFollowStatus = async () => {
      try {
        const data = await getFollowStatusApi({
          followerIdx: loginUserIdx,
          followingIdx: userIdx,
        });
        setFollowStatus(data.followStatus);
      } catch (err) {
        console.log("[팔로우상태] 조회를 실패했습니다");
        console.log(err);
      }
    };
    const getMypageUser = async () => {
      try {
        const data = await getMypageUserApi({ idx: userIdx });
        setNickname(data.nickname);
      } catch (err) {
        console.log("[사용자정보] 조회를 실패했습니다");
        console.log(err);
      }
    };

    if (!isNaN(userIdx)) {
      if (parseInt(userIdx) === parseInt(loginUserIdx)) {
        setNowMenuTab();
      }
      if (parseInt(userIdx) !== parseInt(loginUserIdx)) {
        getFollowStatus();
      }
      getFollowCount();
      getMypageUser();
    }
  }, [userIdx, loginUserIdx, pathname]);

  return (
    <BasicLayout>
      <div className="border border-gray-200 w-full">
        {/* 프로필 정보 */}
        <ProfileComponent
          feedCount={feedCount}
          followCount={followCount}
          nickname={nickname}
        />

        {/* 버튼 */}
        <ButtonComponent
          handleFollowClickEvent={handleFollowClickEvent}
          followStatus={followStatus}
        />

        {/* 메뉴바 */}
        {parseInt(loginUserIdx) === parseInt(userIdx) ? (
          <div
            className="sticky top-0 bg-white border border-gray-200 rounded-lg 
          p-4 mt-4 z-10 shadow-lg z-50"
          >
            <div className="flex space-x-6">
              {menuList.map((menu) => (
                <MenuTabComponent
                  key={menu.id}
                  menu={menu}
                  activeMenu={activeMenu}
                  handleMenuTabClickEvent={handleMenuTabClickEvent}
                />
              ))}
            </div>
          </div>
        ) : (
          <></>
        )}

        {/* 목록 */}
        <Outlet context={{ handleFeedCountEvent }} />
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
