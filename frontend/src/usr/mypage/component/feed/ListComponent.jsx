import { useOutletContext, useParams } from "react-router-dom";
import { useBasic } from "../../../../common/context/BasicContext";
import { useEffect, useRef, useState } from "react";
import { API_BASE_URL, getFeedListApi } from "../../api/MypageApi";
import DetailModal from "../../../feed/modal/DetailModal";
import {
  deleteFeedApi,
  feedLikeApi,
  getFeedDetailApi,
} from "../../../feed/api/FeedApi";
import ModifyModal from "../../../feed/modal/ModifyModal";
import ButtonComponent from "./ButtonComponent";

const initSrc = `${API_BASE_URL}/api/usr/feed/image/no_image.jpg`;

const ListComponent = () => {
  const { handleFeedCountEvent } = useOutletContext();

  const { userIdx } = useParams();
  const { userInfo } = useBasic();
  const loginUserIdx = userInfo?.userIdx || "";

  // 피드목록
  const [feedList, setFeedList] = useState([]);
  const [hasNext, setHasNext] = useState(true);
  const [lastId, setLastId] = useState(0);
  const [loading, setLoading] = useState(false);
  const getFeedList = () => {
    if (!hasNext || loading) {
      return;
    }
    setLoading(true);

    getFeedListApi({ userIdx, lastId: lastId, pageSize: 12 })
      .then((data) => {
        const { feedDTOS, feedCount, hasNext } = data;
        setFeedList((prevList) => [...prevList, ...feedDTOS]);

        feedCountRef.current = feedCount;
        handleFeedCountEvent(feedCount);

        setHasNext(hasNext);
        if (feedDTOS.length > 0) {
          setLastId(parseInt(feedDTOS[feedDTOS.length - 1].idx));
        }
      })
      .catch((err) => {
        console.log("[피드목록] 조회를 실패했습니다");
        console.log(err);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // 피드개수
  const feedCountRef = useRef(0);

  // 피드정보
  const [selectedFeed, setSelectedFeed] = useState({});
  const handleSelectedFeed = (idx) => {
    const feed = feedList.find((item) => item.idx === parseInt(idx));
    setSelectedFeed(feed);
  };

  // 피드상세
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const openDetailModal = (idx) => {
    handleSelectedFeed(idx);
    setIsDetailModalOpen(true);
  };
  const closeDetailModal = () => {
    setIsDetailModalOpen(false);
    setSelectedFeed({});
  };

  // 피드수정
  const [isModifyModalOpen, setIsModifyModalOpen] = useState(false);
  const openModifyModal = (idx) => {
    handleSelectedFeed(idx);
    setIsModifyModalOpen(true);
  };
  const closeModifyModal = (idx) => {
    setIsModifyModalOpen(false);
    setSelectedFeed({});
    idx && handleResearchEvent(idx, "update"); // 목록 재조회
  };

  // 피드 삭제
  const handleListDeleteEvent = (idx) => {
    deleteFeedApi({ idx: idx })
      .then((data) => {
        if (data) {
          setFeedList((prevlist) =>
            prevlist.filter((feed) => feed.idx !== data.idx)
          );

          feedCountRef.current = feedCountRef.current - 1;
          handleFeedCountEvent(feedCountRef.current);
        }
      })
      .catch((err) => {
        console.log("[피드삭제] 삭제를 실패했습니다");
        console.log(err);
      });
  };

  // 데이터 재조회
  const handleResearchEvent = (idx, type) => {
    getFeedDetailApi(idx)
      .then((data) => {
        setFeedList((prevList) => {
          return prevList.map((item) => {
            return item.idx === idx ? { ...item, ...data } : { ...item };
          });
        });
      })
      .catch((err) => {
        console.log("[피드목록] 조회를 실패했습니다");
        console.log(err);
      });
  };

  // 좋아요
  const handleLikesEvent = (targetIdx, likedYn, likeCount) => {
    feedLikeApi({ idx: targetIdx, likedYn })
      .then((data) => {
        const newLikeCount = likeCount > 99 ? "99+" : likeCount;
        setFeedList((prevFeedList) =>
          prevFeedList.map((feed) =>
            feed.idx === targetIdx
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
    setFeedList((prevFeedList) =>
      prevFeedList.map((feed) => {
        if (feed.idx === targetIdx) {
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
    getFeedList();
    window.addEventListener("scroll", handleScrollEvent);
    // window.addEventListener("resize", handleScrollEvent);
    return () => {
      window.removeEventListener("scroll", handleScrollEvent);
      // window.removeEventListener("resize", handleScrollEvent);
    };
  }, []);

  useEffect(() => {
    if (isBottom && hasNext) {
      getFeedList();
    }
  }, [isBottom]);

  return (
    <>
      <div className="mt-8 grid grid-cols-3 gap-4">
        {/* 게시물 카드 */}
        {feedList.map((feed) => (
          <div
            key={feed.idx}
            className="relative h-40 w-full group transition-shadow duration-300"
          >
            <img
              src={
                (feed.feedImages ?? []).length > 0
                  ? `${API_BASE_URL}/api/usr/feed/image/s_${feed.feedImages[0].uploadName}`
                  : initSrc
              }
              alt="업로드 이미지"
              className="w-full h-full object-contain"
            />

            <ButtonComponent
              userIdx={userIdx}
              loginUserIdx={loginUserIdx}
              feedIdx={feed.idx}
              openDetailModal={openDetailModal}
              openModifyModal={openModifyModal}
              handleListDeleteEvent={handleListDeleteEvent}
            />

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
      {(selectedFeed.idx ?? null) && isModifyModalOpen && (
        <ModifyModal feed={selectedFeed} closeModifyModal={closeModifyModal} />
      )}
    </>
  );
};

export default ListComponent;
