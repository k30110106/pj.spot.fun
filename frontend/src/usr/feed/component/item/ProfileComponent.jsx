import React, { useCallback } from "react";

const ProfileComponent = ({ user }) => {
  const { idx, nickname } = user;
  const handleProfileEvent = useCallback(() => {
    console.log("profile idx >>" + idx);
  }, [idx]);

  return (
    <>
      <div
        className="flex items-center space-x-4 cursor-pointer"
        onClick={handleProfileEvent}
      >
        {/* 프로필 */}
        <img src="" alt="프로필 이미지" className="w-12 h-12 rounded-full" />
        <div>
          <p className="font-semibold text-lg">{nickname}</p>
        </div>
      </div>
    </>
  );
};

export default ProfileComponent;
