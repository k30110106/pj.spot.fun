import React, { useCallback, useEffect, useState } from "react";
import { API_BASE_URL, getFeedListApi } from "../api/FeedApi";

const ListItemComponent = () => {
  const [loading, setLoading] = useState(false);
  const [feedList, setFeedList] = useState([]);
  const [lastId, setLastId] = useState(0);
  const [hasNext, setHasNext] = useState(true);
  const [isBottom, setIsBottom] = useState(false);

  // 스크롤 위치
  const botScroll = () => {
    const scrollTop = window.scrollY || document.documentElement.scrollTop;
    const innerHeight = window.innerHeight;
    const scrollHeight = document.documentElement.scrollHeight;

    if (scrollTop + innerHeight >= scrollHeight - 1) {
      setIsBottom(true);
    } else {
      setIsBottom(false);
    }
  };

  // 데이터 처리
  const getList = useCallback(() => {
    if (!hasNext || loading) {
      return;
    }

    setLoading(true);

    const param = { lastId: lastId, pageSize: 1 };
    getFeedListApi(param)
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
  }, [lastId, hasNext, loading]);

  useEffect(() => {
    getList();

    window.addEventListener("scroll", botScroll);
    window.addEventListener("resize", botScroll);

    return () => {
      window.removeEventListener("scroll", botScroll);
      window.removeEventListener("resize", botScroll);
    };
  }, []);

  useEffect(() => {
    if (isBottom) {
      getList();
      setIsBottom(false);
    }
  }, [isBottom, getList]);

  console.log("mount.." + lastId);

  return (
      <div>
        <h1>Feed List</h1>
        <div>
        {feedList && feedList.length > 0 ? (
            feedList.map((feed) => (
                <ul key={`feed_${feed.idx}`}>
                  <li>{feed.idx}</li>
                  <li>{feed.content}</li>
                  <li>
                    {feed.feedImages && feed.feedImages.length > 0 ? (
                        feed.feedImages.map((image) => (
                            <img
                                key={`img_${image.idx}`}
                                alt={image.originName}
                                className="m-auto rounded-md w-60"
                                src={`${API_BASE_URL}/api/usr/feed/image/${image.uploadName}`}
                                style={{ width: "500px", height: "500px" }}
                            />
                        ))
                    ) : (
                        <img
                            alt="no_image"
                            className="m-auto rounded-md w-60"
                            src={`${API_BASE_URL}/api/usr/feed/image/no_image.jpg`}
                        />
                    )}
                  </li>
                </ul>
            ))
        ) : (
            <div>NO Data</div>
        )}
        </div>
        {loading && <h1>Loading more...</h1>}
      </div>
  );
};

export default ListItemComponent;
