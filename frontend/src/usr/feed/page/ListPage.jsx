import { useEffect, useState } from "react";
import { feedLikeApi, getFeedListApi } from "../api/FeedApi";
import ListComponent from "../component/ListComponent";
import InsertModal from "../modal/InsertModal";
import DetailModal from "../modal/DetailModal";

const ListPage = () => {
  // 로그인 상태
  const [isLogin, setIsLogin] = useState(false);
  // const checkLoginToken = async () => {
  //   const result = await checkToken();
  //   //console.log("isLogin result >> "+result);
  //   setIsLogin(result);
  // };

  // 모달
  const [isInsertModalOpen, setIsInsertModalOpen] = useState(false);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const closeInsertModal = () => {
    setIsInsertModalOpen(false);
  };
  const openDetailModal = () => {
    setIsDetailModalOpen(true);
  };
  const closeDetailModal = () => {
    setIsDetailModalOpen(false);
  };

  // 스크롤 위치
  const [isBottom, setIsBottom] = useState(false);
  const handelScrollEvent = () => {
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

  // 상세조회
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
        console.log(data);
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

  // 최초 마운트
  useEffect(() => {
    //checkLoginToken();
    handleListEvent();

    window.addEventListener("scroll", handelScrollEvent);
    // window.addEventListener("resize", handelScrollEvent);

    return () => {
      window.removeEventListener("scroll", handelScrollEvent);
      // window.removeEventListener("resize", handelScrollEvent);
    };
  }, []);

  // 모달상태에서 스크롤 비활성화
  useEffect(() => {
    if (isInsertModalOpen || isDetailModalOpen) {
      document.body.style.overflow = "hidden";
      //checkLoginToken(); // 로그인 상태 확인
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
      //checkLoginToken();
      handleListEvent();
    }
  }, [isBottom, isLogin]);

  return (
    <div className="border border-amber-200 w-1/2">
      피드 리스트 페이지
      <button
        type="button"
        className="bg-blue-500 text-white border-2 border-black"
        onClick={() => {
          setIsInsertModalOpen(true);
        }}
      >
        !!!! 등록 버튼 (비로그인은 숨겨야함)!!!!
      </button>
      <ListComponent
        isLogin={isLogin}
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
        />
      )}
    </div>
  );
};

export default ListPage;
