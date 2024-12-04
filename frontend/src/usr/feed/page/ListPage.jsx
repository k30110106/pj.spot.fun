import { useEffect, useState } from "react";
import { feedLikeApi, getFeedDetailApi, getFeedListApi } from "../api/FeedApi";
import ListComponent from "../component/ListComponent";
import InsertModal from "../modal/InsertModal";
import DetailModal from "../modal/DetailModal";
import { useBasic } from "../../../common/context/BasicContext";

const ListPage = () => {
  const { userInfo } = useBasic();
  //console.log(userInfo);

  // 모달
  const [isInsertModalOpen, setIsInsertModalOpen] = useState(false);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const closeInsertModal = (newIdx) => {
    setIsInsertModalOpen(false);
    newIdx && handleDetailEvent(newIdx); // 목록 재조회
  };
  const openDetailModal = () => {
    setIsDetailModalOpen(true);
  };
  const closeDetailModal = () => {
    setIsDetailModalOpen(false);
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

  // 목록 조회
  const [feedList, setFeedList] = useState([]);
  const [hasNext, setHasNext] = useState(true);
  const [lastId, setLastId] = useState(0);
  const [loading, setLoading] = useState(false);
  const handleListEvent = () => {
    if (!hasNext || loading) {
      return;
    }
    setLoading(true);
    getFeedListApi({ lastId: lastId, pageSize: 1 })
      .then((data) => {
        const { feedDTOS, hasNext } = data;
        setFeedList((prevList) => [...prevList, ...feedDTOS]);
        setHasNext(hasNext);
        if (feedDTOS.length > 0) {
          setLastId(parseInt(feedDTOS[feedDTOS.length - 1].idx));
        }
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // 상세정보 조회
  const handleDetailEvent = (idx) => {
    getFeedDetailApi(idx)
      .then((data) => {
        console.log(data);
        setFeedList((prevList) => [data, ...prevList]);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 상세모달 조회
  const [selectedFeed, setSelectedFeed] = useState({});
  const handleSelectedFeed = (idx) => {
    const feed = feedList.find((item) => item.idx === idx);
    setSelectedFeed(feed);
    openDetailModal();
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
        console.log(err);
      });
  };

  // 댓글 수
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

  // 최초 마운트
  useEffect(() => {
    handleListEvent();
    window.addEventListener("scroll", handleScrollEvent);
    // window.addEventListener("resize", handleScrollEvent);
    return () => {
      window.removeEventListener("scroll", handleScrollEvent);
      // window.removeEventListener("resize", handleScrollEvent);
    };
  }, []);

  // 모달상태에서 스크롤 비활성화
  useEffect(() => {
    if (isInsertModalOpen || isDetailModalOpen) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }
    return () => {
      document.body.style.overflow = "auto";
    };
  }, [isInsertModalOpen, isDetailModalOpen]);

  // 목록 조회
  useEffect(() => {
    if (isBottom && hasNext) {
      handleListEvent();
    }
  }, [isBottom]);

  return (
    <div className="border border-amber-200 w-full">
      피드 리스트 페이지
      {userInfo != null && (
        <button
          type="button"
          className="bg-blue-500 text-white border-2 border-black"
          onClick={() => {
            setIsInsertModalOpen(true);
          }}
        >
          !!!! 등록 버튼 (비로그인은 숨겨야함)!!!!
        </button>
      )}
      <ListComponent
        feedList={feedList}
        handleSelectedFeed={handleSelectedFeed}
        handleLikesEvent={handleLikesEvent}
      />
      {isInsertModalOpen && <InsertModal closeInsertModal={closeInsertModal} />}
      {(selectedFeed.idx ?? null) && isDetailModalOpen && (
        <DetailModal
          feed={selectedFeed}
          closeDetailModal={closeDetailModal}
          handleLikesEvent={handleLikesEvent}
          handleCommentCountEvent={handleCommentCountEvent}
        />
      )}
    </div>
  );
};

export default ListPage;
