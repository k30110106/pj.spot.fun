import BasicLayout from "../../../common/layout/BasicLayout";
import { Outlet, useLocation, useNavigate, useParams } from "react-router-dom";
import ProfileComponent from "../component/ProfileComponent";
import ButtonComponent from "../component/ButtonComponent";
import { useEffect, useState } from "react";
import MenuTabComponent from "../component/MenuTabComponent";
import { useBasic } from "../../../common/context/BasicContext";

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

  useEffect(() => {
    const setNowMenuTab = () => {
      menuList.map(
        (menu) => pathname.includes(menu.id) && setActiveMenu(menu.id)
      );
    };

    if (parseInt(userIdx) === parseInt(loginUserIdx)) {
      setNowMenuTab();
    }
  }, [userIdx, loginUserIdx, pathname]);

  return (
    <BasicLayout>
      <div className="border border-gray-200 w-full">
        {/* 프로필 정보 */}
        <ProfileComponent feedCount={feedCount} />

        {/* 버튼 */}
        <ButtonComponent />

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
