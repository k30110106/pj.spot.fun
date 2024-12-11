import { useParams } from "react-router-dom";
import { useBasic } from "../../../common/context/BasicContext";

const ButtonComponent = ({ handleFollowClickEvent, followStatus }) => {
  const { userIdx } = useParams();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  return (
    <>
      <div className="mt-4">
        <div className="mt-2 flex space-x-4">
          {parseInt(userIdx) === parseInt(loginUserIdx) ? (
            <>
              <button
                className="bg-blue-500 text-white py-1 px-4 rounded-full text-sm hover:bg-blue-600"
                onClick={null}
              >
                프로필 편집
              </button>
            </>
          ) : (
            <>
              <button
                className={`
                  ${
                    followStatus
                      ? "bg-blue-500 text-white hover:bg-blue-600 "
                      : "bg-green-500 text-white hover:bg-green-600 "
                  }
                  py-1 px-4 rounded-full text-sm`}
                onClick={handleFollowClickEvent}
              >
                {followStatus ? "팔로우 중" : "팔로우"}
              </button>
              <button
                className="bg-gray-200 text-gray-800 py-1 px-4 rounded-full text-sm hover:bg-gray-300"
                onClick={null}
              >
                메시지
              </button>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default ButtonComponent;
