const ProfileComponent = () => {
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
        <div className="text-xl font-semibold">username</div>
        <div className="text-sm text-gray-500">이름</div>
        <div className="mt-2 flex space-x-6">
          <div className="text-center">
            <div className="font-semibold">500</div>
            <div className="text-sm text-gray-500">팔로워</div>
          </div>
          <div className="text-center">
            <div className="font-semibold">180</div>
            <div className="text-sm text-gray-500">팔로잉</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileComponent;
