import React, { useEffect, useState } from "react";
import { API_BASE_URL, getLikeListApi } from "../../api/MypageApi";
import { useParams } from "react-router-dom";
import { useBasic } from "../../../../common/context/BasicContext";
import { FaPlus } from "react-icons/fa";
import DetailModal from "../../../feed/modal/DetailModal";
import { feedLikeApi } from "../../../feed/api/FeedApi";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ListComponent = () => {
  const { userIdx } = useParams();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  // 좋아요 목록
  const [likeList, setLikeList] = useState([]);
  const [hasNext, setHasNext] = useState(true);
  const [lastId, setLastId] = useState(0);
  const [loading, setLoading] = useState(false);
  const getLikeList = () => {
    if (!hasNext || loading) {
      return;
    }
    setLoading(true);

    getLikeListApi({ userIdx, lastId: lastId, pageSize: 12 })
      .then((data) => {
        const { feedDTOS, hasNext } = data;
        setLikeList((prevList) => [...prevList, ...feedDTOS]);

        setHasNext(hasNext);
        if (feedDTOS.length > 0) {
          setLastId(parseInt(feedDTOS[feedDTOS.length - 1].idx));
        }
      })
      .catch((err) => {
        console.log("[좋아요목록] 조회를 실패했습니다");
        console.log(err);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // 피드정보
  const [selectedFeed, setSelectedFeed] = useState({});
  const handleSelectedFeed = (idx) => {
    const feed = likeList.find((item) => item.idx === parseInt(idx));
    setSelectedFeed({ ...feed, idx: feed.feedIdx });
  };

  // 피드상세
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const openDetailModal = (idx) => {
    handleSelectedFeed(idx);
    setIsDetailModalOpen(true);
  };
  const closeDetailModal = () => {
    setIsDetailModalOpen(false);
    handleLikeListEvent();
  };

  // 좋아요 상태체크
  const handleLikeListEvent = () => {
    const { feedIdx, likedYn } = selectedFeed;

    if (!likedYn) {
      setLikeList((prevList) =>
        prevList.filter((item) => item.feedIdx !== feedIdx)
      );
    }

    setSelectedFeed({});
  };

  // 좋아요
  const handleLikesEvent = (targetIdx, likedYn, likeCount) => {
    feedLikeApi({ idx: targetIdx, likedYn })
      .then((data) => {
        const newLikeCount = likeCount > 99 ? "99+" : likeCount;
        setLikeList((prevFeedList) =>
          prevFeedList.map((feed) =>
            feed.feedIdx === targetIdx
              ? { ...feed, likedYn: likedYn, likeCount: newLikeCount }
              : feed
          )
        );
        setSelectedFeed((prevFeed) => ({
          ...prevFeed,
          likedYn: likedYn,
          likeCount: newLikeCount,
        }));
      })
      .catch((err) => {
        console.log("[좋아요] 등록을 실패했습니다");
        console.log(err);
      });
  };

  // 댓글수정
  const handleCommentCountEvent = (targetIdx, type) => {
    setLikeList((prevFeedList) =>
      prevFeedList.map((feed) => {
        if (feed.feedIdx === targetIdx) {
          const commentCount =
            type === "new" ? feed.commentCount + 1 : feed.commentCount - 1;
          const newCommentCount = commentCount > 99 ? "99+" : commentCount;
          setSelectedFeed((prevFeed) => ({
            ...prevFeed,
            commentCount: newCommentCount,
          }));
          return { ...feed, commentCount: newCommentCount };
        }
        return feed;
      })
    );
  };

  // 스크롤 위치
  const [isBottom, setIsBottom] = useState(false);
  const handleScrollEvent = () => {
    const scrollTop = window.scrollY || document.documentElement.scrollTop;
    const innerHeight = window.innerHeight;
    const scrollHeight = document.documentElement.scrollHeight;

    if (scrollTop + innerHeight >= scrollHeight - 1) {
      setIsBottom(true);
    } else {
      setIsBottom(false);
    }
  };

  useEffect(() => {
    getLikeList();
    window.addEventListener("scroll", handleScrollEvent);
    // window.addEventListener("resize", handleScrollEvent);
    return () => {
      window.removeEventListener("scroll", handleScrollEvent);
      // window.removeEventListener("resize", handleScrollEvent);
    };
  }, []);

  useEffect(() => {
    if (isBottom && hasNext) {
      getLikeList();
    }
  }, [isBottom]);

  return (
    <>
      <div className="mt-8 grid grid-cols-3 gap-4">
        {/* 게시물 카드 */}
        {likeList.map((feed) => (
          <div
            key={feed.idx}
            className="relative h-40 w-full group transition-shadow duration-300"
          >
            <img
              src={
                (feed.feedImages ?? []).length > 0
                  ? `${API_BASE_URL}/api/usr/feed/image/${feed.feedImages[0].uploadName}`
                  : initSrc
              }
              alt="업로드 이미지"
              className="w-full h-full object-contain"
            />

            <div
              className="absolute inset-0 flex justify-center items-center opacity-0 
            group-hover:opacity-100 transition-opacity duration-300 bg-gray-500 bg-opacity-30 z-10 
            cursor-pointer"
              onClick={() => openDetailModal(`${feed.idx}`)}
            >
              <div className="text-white flex items-center">
                <FaPlus size="1.2rem" />
              </div>
            </div>

            <div className="absolute inset-0 group-hover:shadow-lg group-hover:shadow-gray-400 transition-shadow duration-300"></div>
          </div>
        ))}
      </div>

      {(selectedFeed.idx ?? null) && isDetailModalOpen && (
        <DetailModal
          feed={selectedFeed}
          closeDetailModal={closeDetailModal}
          handleLikesEvent={handleLikesEvent}
          handleCommentCountEvent={handleCommentCountEvent}
        />
      )}
    </>
  );
};

export default ListComponent;
