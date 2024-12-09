import BasicLayout from "../../../common/layout/BasicLayout";
import { Outlet, useNavigate, useParams } from "react-router-dom";
import ProfileComponent from "../component/ProfileComponent";
import ButtonComponent from "../component/ButtonComponent";
import { useState } from "react";
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

  // 탭 메뉴
  const navigate = useNavigate();
  const [activeMenu, setActiveMent] = useState(initMenu);
  const handleMenuTabClickEvent = (id) => {
    navigate(`/mypage/${id}/${userIdx}`);
    setActiveMent(id);
  };

  return (
    <BasicLayout>
      <div className="border border-gray-200 w-full">
        {/* 프로필 정보 */}
        <ProfileComponent />

        {/* 버튼 */}
        <ButtonComponent />

        <div className="sticky top-0 bg-white border border-gray-200 rounded-lg p-4 mt-4 z-10 shadow-lg">
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

        {/* 목록 */}
        <Outlet />
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
