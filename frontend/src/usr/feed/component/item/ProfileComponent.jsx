import { useCallback } from "react";
import { useBasic } from "../../../../common/context/BasicContext";
import { useNavigate } from "react-router-dom";
import { API_BASE_URL } from "../../api/FeedApi";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ProfileComponent = ({
  feedUserInfo,
  pageType,
  handleListDeleteEvent,
  openModifyModal,
}) => {
  const navigate = useNavigate();
  const { userIdx, uploadName, user } = feedUserInfo;

  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  const handleProfileEvent = useCallback(() => {
    if (loginUserIdx !== "" && loginUserIdx !== userIdx) {
      navigate(`/mypage/feed/${userIdx}`);
    }
  }, [userIdx, loginUserIdx]);

  return (
    <>
      <div className="flex items-center justify-between px-3">
        {/* 프로필 정보 왼쪽 */}
        <div
          className={`
            ${
              loginUserIdx !== "" && loginUserIdx !== userIdx
                ? "cursor-pointer"
                : ""
            }
            flex items-center space-x-4`}
          onClick={handleProfileEvent}
        >
          <img
            src={
              uploadName
                ? `${API_BASE_URL}/api/usr/profile/image/s_${uploadName}`
                : initSrc
            }
            alt="프로필 이미지"
            className="w-12 h-12 rounded-full"
          />
          <div>
            <p className="font-semibold text-lg">{user?.nickname}</p>
          </div>
        </div>

        {/* 수정, 삭제 버튼 오른쪽 */}
        <div className="space-x-4">
          {pageType === "list" && loginUserIdx === userIdx && (
            <>
              <button className="text-blue-500" onClick={openModifyModal}>
                수정
              </button>
              <span className="mx-2 text-gray-500">|</span>
              <button className="text-red-500" onClick={handleListDeleteEvent}>
                삭제
              </button>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default ProfileComponent;
