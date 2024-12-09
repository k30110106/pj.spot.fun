import { useParams } from "react-router-dom";
import { useBasic } from "../../../../common/context/BasicContext";
import { useCallback, useEffect } from "react";
import { getFeedListApi } from "../../api/MypageApi";

const ListComponent = () => {
  const { userIdx } = useParams();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  const getFeedList = useCallback(() => {
    getFeedListApi({ userIdx })
      .then((data) => {
        console.log(data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [userIdx]);

  useEffect(() => {
    getFeedList();
  }, []);

  return (
    <div className="mt-8 grid grid-cols-3 gap-4">
      {/* 게시물 카드 */}
      {[1, 2, 3, 4, 5, 6].map((item) => (
        <div key={item} className="relative">
          <img
            src={`post-image-url-${item}`} // 게시물 이미지 URL
            alt={`Post ${item}`}
            className="w-full h-full object-cover rounded-lg"
          />
        </div>
      ))}
    </div>
  );
};

export default ListComponent;
