import { useBasic } from "../../../common/context/BasicContext";

const ProfileComponent = ({ feedCount, followCount }) => {
  const { userInfo } = useBasic();
  const nickname = userInfo?.nickname || "";

  return (
    <div className="flex items-center space-x-8 py-4 px-6">
      {/* 프로필 이미지 */}
      <div className="w-32 h-32 rounded-full bg-gray-300">
        <img
          src=""
          alt="프로필 이미지"
          className="w-full h-full object-cover"
        />
      </div>

      {/* 사용자 정보 */}
      <div className="flex flex-col justify-center">
        <div className="text-xl font-semibold">{nickname}</div>
        {/* <div className="text-sm text-gray-500">이름</div> */}
        <div className="mt-2 flex space-x-6">
          <div className="text-center">
            <div className="font-semibold">{feedCount}</div>
            <div className="text-sm text-gray-500">피드</div>
          </div>
          <div className="text-center">
            <div className="font-semibold">{followCount.followerCount}</div>
            <div className="text-sm text-gray-500">팔로워</div>
          </div>
          <div className="text-center">
            <div className="font-semibold">{followCount.followingCount}</div>
            <div className="text-sm text-gray-500">팔로잉</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileComponent;
