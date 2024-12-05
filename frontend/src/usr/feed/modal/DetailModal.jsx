import { useEffect, useState, useCallback } from "react";
import {
  deleteCommentApi,
  getFeedCommentListApi,
  postCommentApi,
  postReplyApi,
  putCommentApi,
} from "../api/FeedApi";
import ImageComponent from "../component/item/ImageComponent";
import ProfileComponent from "../component/item/ProfileComponent";
import ContentComponent from "../component/item/ContentComponent";
import ButtonComponent from "../component/item/ButtonComponent";
import CommentListComponent from "../component/comment/CommentListComponent";
import InputComponent from "../component/comment/InputComponent";
import { useBasic } from "../../../common/context/BasicContext";

const DetailModal = ({
  feed,
  closeDetailModal,
  handleLikesEvent,
  handleCommentCountEvent,
}) => {
  const { userInfo } = useBasic();
  //console.log(userInfo);

  // 댓글목록 조회
  const [commentList, setCommentList] = useState([]);
  useEffect(() => {
    if (feed) {
      getFeedCommentListApi(feed.idx)
        .then((data) => {
          setCommentList(data);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, []);

  // 댓글 등록
  const handleCommentEvent = useCallback(
    (content) => {
      const param = {
        feedIdx: feed.idx,
        content,
      };

      postCommentApi(param)
        .then((data) => {
          if (data) {
            setCommentList((prevCommentList) => [...prevCommentList, data]);
            handleCommentCountEvent(feed.idx, "new");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    [feed.idx]
  );

  // 댓글 수정
  const handleCommentModifyEvent = (param) => {
    putCommentApi(param)
      .then((data) => {
        if (data) {
          setCommentList((prevList) => {
            return prevList.map((item) => {
              if (item.idx === data.idx) {
                return { ...item, content: data.content };
              }
              return { ...item };
            });
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 댓글 삭제
  const handleCommentDeleteEvent = (idx) => {
    deleteCommentApi({ idx: idx })
      .then((data) => {
        if (data) {
          setCommentList((prevList) => {
            return prevList.filter((item) => item.idx !== data.idx);
          });
          handleCommentCountEvent(feed.idx, "delete");
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 답글 등록
  const handleReplyEvent = useCallback(
    (obj) => {
      const param = { ...obj, feedIdx: feed.idx };
      //console.log(param);
      postReplyApi(param)
        .then((data) => {
          setCommentList((prevList) => {
            return prevList.map((item) => {
              if (item.idx === data.parentIdx) {
                return {
                  ...item,
                  replyList:
                    item.replyList != null ? [...item.replyList, data] : [data],
                };
              }
              return item;
            });
          });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    [feed.idx]
  );

  // 답글 수정
  const handleReplyModifyEvent = (obj) => {
    const param = { ...obj, feedIdx: feed.idx };
    putCommentApi(param)
      .then((data) => {
        //console.log(data);
        if (data) {
          setCommentList((prevList) => {
            return prevList.map((comment) => {
              if (comment.idx === param.parentIdx) {
                return {
                  ...comment,
                  replyList: comment.replyList.map((reply) => {
                    if (reply.idx === data.idx) {
                      return { ...reply, content: data.content };
                    }
                    return { ...reply };
                  }),
                };
              }
              return { ...comment };
            });
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // 답글 삭제
  const handleReplyDeleteEvent = (idx, parentIdx) => {
    deleteCommentApi({ idx: idx })
      .then((data) => {
        //console.log(data);
        if (data) {
          setCommentList((prevList) => {
            return prevList.map((comment) => {
              if (comment.idx === parentIdx) {
                return {
                  ...comment,
                  replyList: comment.replyList.filter(
                    (reply) => reply.idx !== idx
                  ),
                };
              }
              return { ...comment };
            });
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg w-9/12 h-5/6 p-6 flex">
        <div className="w-3/5 relative">
          {/* 왼쪽 영역 */}
          {/* 이미지 */}
          {(feed.feedImages ?? []).length > 0 && (
            <ImageComponent feedImages={feed.feedImages} />
          )}
        </div>

        <div className="w-2/5 pl-3 h-full flex flex-col justify-center">
          {/* 오른쪽 영역 */}
          <div className="flex justify-between mb-4 items-start">
            {/* 프로필 */}
            {feed.user && <ProfileComponent user={feed.user} />}

            {/* 상단 툴바 X 버튼 */}
            <button
              onClick={closeDetailModal}
              className="text-gray-500 hover:text-gray-800"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>

          {/* 컨텐츠 + 해시태그 */}
          <ContentComponent feed={feed} />

          {/* 버튼 + 등록일시 */}
          <ButtonComponent feed={feed} handleLikesEvent={handleLikesEvent} />

          <div className="border border-gray-200 overflow-y-auto p-4 space-y-4 h-full">
            {/* 댓글 목록 + 등록인풋 */}
            <CommentListComponent
              commentList={commentList}
              handleReplyEvent={handleReplyEvent}
              handleCommentModifyEvent={handleCommentModifyEvent}
              handleCommentDeleteEvent={handleCommentDeleteEvent}
              handleReplyModifyEvent={handleReplyModifyEvent}
              handleReplyDeleteEvent={handleReplyDeleteEvent}
            />
          </div>

          <div className="flex justify-center w-full">
            {/*댓글 입력 + 등록*/}
            {userInfo != null && (
              <InputComponent handleCommentEvent={handleCommentEvent} />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DetailModal;
