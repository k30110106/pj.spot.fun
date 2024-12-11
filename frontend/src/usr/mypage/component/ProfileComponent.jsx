import { API_BASE_URL } from "../api/MypageApi";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ProfileComponent = ({ feedCount, followCount, mypageInfo }) => {
  console.log(mypageInfo);
  const { uploadName, description, user } = mypageInfo;

  return (
    <div className="flex items-center space-x-8 py-4 px-6">
      {/* 프로필 이미지 */}
      <div className="w-32 h-32 rounded-full bg-gray-300 overflow-hidden">
        <img
          src={
            uploadName
              ? `${API_BASE_URL}/api/usr/profile/image/${uploadName}`
              : initSrc
          }
          alt="프로필 이미지"
          className="w-full h-full object-cover rounded-full"
        />
      </div>

      {/* 사용자 정보 */}
      <div className="flex flex-col justify-center">
        <div className="text-xl font-semibold">{user?.nickname}</div>
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
