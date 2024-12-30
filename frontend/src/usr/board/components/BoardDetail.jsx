import React, {useState, useEffect} from "react";
import {useParams, useNavigate} from "react-router-dom";
import axios from "axios";
import {useBasic} from "../../../common/context/BasicContext"; // 로그인 정보 가져오기
import "./BoardDetail.css"; // CSS 파일 추가

const BoardDetail = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState(null);
    const [hasLiked, setHasLiked] = useState(false);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState("");
    const [replyContent, setReplyContent] = useState({});
    const {userInfo} = useBasic();
    const [replyVisibility, setReplyVisibility] = useState({}); // 대댓글 입력창 표시 상태

    const handleReplyToggle = (commentId) => {
        setReplyVisibility((prev) => ({
            ...prev,
            [commentId]: !prev[commentId], // 토글 방식으로 입력창 표시 여부 변경
        }));
    };

    // 게시글 가져오기
    useEffect(() => {
        axios
            .get(`http://localhost:8080/api/boards/${id}`)
            .then((response) => {
                setBoard(response.data);
            })
            .catch((error) => {
                console.error("Error fetching board:", error);
            });
    }, [id]);

    // 게시글 추천 관리
    useEffect(() => {
        const fetchData = async () => {
            try {

                // 게시글 데이터 가져오기
                const boardResponse = await axios.get(`http://localhost:8080/api/boards/${id}`);
                setBoard(boardResponse.data);

                // 추천 여부 확인
                if (userInfo?.userIdx) {
                    const likeResponse = await axios.get(
                        `http://localhost:8080/api/boards/${id}/has-liked`,
                        {
                            params: {userIdx: userInfo?.userIdx}, // userIdx 전달
                        }
                    );
                    setHasLiked(likeResponse.data.hasLiked); // 서버에서 반환된 true/false 값 설정
                }
            } catch (error) {
                console.error("Error fetching board or like status:", error);
            }
        };

        fetchData();
    }, [id, userInfo]);

    // 게시글 조회수 관리
    useEffect(() => {
        const fetchData = async () => {
            try {
                // 게시글 데이터 가져오기
                const boardResponse = await axios.get(`http://localhost:8080/api/boards/${id}`);
                setBoard(boardResponse.data);
                console.log(userInfo?.userIdx ?? 0);
                // 조회수 증가 요청

                await axios.get(`http://localhost:8080/api/boards/${id}/view`, {
                    params: {userIdx: userInfo?.userIdx ?? 0},
                });

            } catch (error) {
                console.error("Error fetching board or incrementing view count:", error);
            }
        };

        fetchData();
    }, [id, userInfo]);

    const formatDateTime = (dateString) => {
        if (!dateString) return "정보 없음";
        const date = new Date(dateString);
        const options = {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
        };
        return new Intl.DateTimeFormat("ko-KR", options).format(date);
    };

    // 댓글 가져오기
    useEffect(() => {
        fetchComments();
    }, [id]);

    const fetchComments = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/comments/${id}`);
            setComments(
                Array.isArray(response.data)
                    ? response.data.map((comment) => ({
                        ...comment,
                        author: comment?.author || "익명",
                        replies: comment?.replies?.map((reply) => ({
                            ...reply,
                            author: reply?.author || "익명", // 대댓글에도 기본값 설정
                        })) || [],
                    }))
                    : []
            );
        } catch (error) {
            console.error("Error fetching comments:", error.response || error);
        }
    };


    const handleCommentSubmit = async () => {
        if (!newComment.trim()) return;

        try {
            const payload = {
                content: newComment,
                author: userInfo.nickname, // 사용자 닉네임
            };
            console.log("Sending Payload:", payload);

            const response = await axios.post(
                `http://localhost:8080/api/comments/${id}`,
                payload,
                {
                    headers: {Authorization: `Bearer ${localStorage.getItem("authToken")}`},
                }
            );

            console.log("Response Data:", response.data); // 응답 데이터 확인
            setNewComment("");
            fetchComments(); // 댓글 새로고침
        } catch (error) {
            console.error("Error posting comment:", error.response || error);
        }
    };

    const handleReplySubmit = async (parentCommentId) => {
        const content = replyContent[parentCommentId]?.trim();
        if (!content) return;

        try {
            const payload = {
                content,
                author: userInfo.nickname, // 사용자 닉네임
                parentCommentId,
            };

            const response = await axios.post(
                `http://localhost:8080/api/comments/${id}`, // 경로 확인
                payload,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("authToken")}`, // 인증 토큰 추가
                    },
                }
            );

            // 댓글 상태 업데이트
            setComments((prevComments) =>
                prevComments.map((comment) =>
                    comment.id === parentCommentId
                        ? {...comment, replies: [...(comment.replies || []), response.data]}
                        : comment
                )
            );

            setReplyContent((prev) => ({...prev, [parentCommentId]: ""}));
            setReplyVisibility((prev) => ({...prev, [parentCommentId]: false})); // 입력창 숨기기
        } catch (error) {
            console.error("Error posting reply:", error);
        }
    };

    const handleEdit = () => {
        navigate(`/board/edit/${id}`); // 수정 화면으로 이동
    };

    const handleDelete = async () => {
        const confirmDelete = window.confirm("정말로 이 게시글을 삭제하시겠습니까?");
        if (!confirmDelete) return;

        try {
            await axios.patch(
                `http://localhost:8080/api/boards/${id}/delete`,
                {},
                {
                    headers: {},
                }
            );
            alert("게시글이 삭제되었습니다.");
            navigate("/board");
        } catch (error) {
            console.error("게시글 삭제 실패:", error.response || error);
            alert("게시글 삭제에 실패했습니다.");
        }
    };

    const handleLike = async () => {
        if (hasLiked) return; // 이미 추천한 경우 실행 방지

        try {
            await axios.post(
                `http://localhost:8080/api/boards/${id}/like`,
                null,
                {
                    params: {userIdx: userInfo?.userIdx}, // userIdx 전달
                }
            );

            // 추천 후 상태 업데이트
            setHasLiked(true);
            setBoard((prevBoard) => ({
                ...prevBoard,
                likeCount: (prevBoard.likeCount || 0) + 1, // 추천 수 증가
            }));
        } catch (error) {
            console.error("추천 실패:", error);
        }
    };


    if (!board) {
        return <p>Loading...</p>;
    }

    const handleDeleteComment = async (commentId) => {
        const isParentComment = comments.some((comment) => comment.id === commentId);
        const targetComment = isParentComment
            ? comments.find((comment) => comment.id === commentId)
            : comments
                .flatMap((comment) => comment.replies)
                .find((reply) => reply.id === commentId);

        if (!targetComment) {
            alert("삭제하려는 댓글을 찾을 수 없습니다.");
            return;
        }

        if (targetComment.author !== userInfo.nickname) {
            alert("댓글 작성자만 삭제할 수 있습니다.");
            return;
        }

        try {
            // 서버에 삭제 요청
            await axios.delete(`http://localhost:8080/api/comments/${commentId}`, {
                headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` },
            });

            // 상태 업데이트
            setComments((prevComments) =>
                prevComments
                    .filter((comment) => comment.id !== commentId) // 상위 댓글 제거
                    .map((comment) => ({
                        ...comment,
                        replies: comment.replies.filter((reply) => reply.id !== commentId), // 대댓글 제거
                    }))
            );
        } catch (error) {
            console.error("Failed to delete comment:", error.response || error);
        }
    };



    return (
        <div
            className="container mx-auto p-4 border rounded-md shadow-sm bg-white"
            style={{borderColor: "#25E2B6"}}
        >
            {/* 제목 */}
            <h1 className="text-3xl font-bold text-gray-800 mb-6">{board.title}</h1>

            {/* 작성일 */}
            <p className="text-sm text-gray-500 mb-1">
                작성일: {formatDateTime(board.regDate)}
            </p>

            {/* 닉네임 */}
            <p className="text-sm text-gray-600 mb-4">작성자: {board.nickname}</p>

            {/* 수정일 */}
            {board.modDate && (
                <p className="text-sm text-gray-500 mb-4">
                    수정일: {formatDateTime(board.modDate)}
                </p>
            )}

            {/* 내용 */}
            <div
                className="board-content text-gray-700 leading-relaxed mb-6"
                dangerouslySetInnerHTML={{__html: board.content}}
            />

            {/* 추천 버튼 */}
            <div className="flex justify-between mb-6">
                <button
                    onClick={handleLike}
                    disabled={hasLiked} // 추천 완료 시 비활성화
                    className={`px-4 py-2 rounded-md transition ${
                        hasLiked
                            ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                            : "bg-green-500 text-white hover:bg-green-600"
                    }`}
                >
                    {hasLiked ? "추천 완료" : "추천"} ({board.likeCount || 0})
                </button>
            </div>


            {/* 수정/삭제 버튼 */}
            {userInfo?.nickname === board.nickname && (
                <div className="flex justify-end gap-4 mb-6">
                    <button
                        onClick={handleEdit}
                        className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition"
                    >
                        수정
                    </button>
                    <button
                        onClick={handleDelete}
                        className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition"
                    >
                        삭제
                    </button>
                </div>
            )}

            <div className="comments-section mt-8">
                <h2 className="text-xl font-semibold mb-4">댓글</h2>
                {comments.length > 0 ? (
                    comments.map((comment) => (
                        <div key={`comment-${comment.id}`} className="mb-4 p-3 border rounded-md">
                            <div className="flex items-center justify-between">
                                <span className="text-sm font-semibold mr-4">{comment.author}</span>
                                <span
                                    className="text-gray-700 flex-1 mr-4 truncate cursor-pointer"
                                    onClick={() => handleReplyToggle(comment.id)}
                                >
                        {comment.content}
                    </span>
                                <div className="flex items-center">
                                    <span
                                        className="text-xs text-gray-500 mr-2">{formatDateTime(comment.createdAt)}</span>
                                    {comment.author === userInfo.nickname && (
                                        <button
                                            className="text-red-500 text-sm font-bold"
                                            onClick={() => handleDeleteComment(comment.id)}
                                        >
                                            X
                                        </button>
                                    )}
                                </div>
                            </div>

                            {/* 대댓글 입력창 */}
                            {replyVisibility[comment.id] && (
                                <div className="mt-2">
                                    <input
                                        type="text"
                                        value={replyContent[comment.id] || ""}
                                        onChange={(e) => {
                                            const input = e.target.value;
                                            setReplyContent((prev) => ({
                                                ...prev,
                                                [comment.id]: input.length <= 50 ? input : input.slice(0, 50),
                                            }));
                                        }}
                                        placeholder="대댓글 입력 (최대 50자)"
                                        className="border rounded-md px-3 py-1 w-full"
                                    />
                                    <button
                                        onClick={() => handleReplySubmit(comment.id)}
                                        className="mt-2 bg-green-500 text-white px-4 py-2 rounded-md"
                                    >
                                        대댓글 작성
                                    </button>
                                </div>
                            )}

                            {/* 대댓글 렌더링 */}
                            {comment.replies &&
                                comment.replies.map((reply) => (
                                    <div
                                        key={`reply-${reply.id}`}
                                        className="mt-4 ml-6 p-3 border rounded-md comment-replies"
                                    >
                                        <div className="flex items-center justify-between">
                                            <span className="text-sm font-semibold">{reply.author}</span>
                                            <span className="text-gray-700 flex-1 ml-2">
                    {reply.content.length > 60
                        ? `${reply.content.slice(0, 60)}...`
                        : reply.content}
                </span>
                                            <div className="flex items-center">
                                                <span className="text-xs text-gray-500 mr-2">{formatDateTime(reply.createdAt)}</span>
                                                {reply.author === userInfo.nickname && (
                                                    <button
                                                        className="text-red-500 text-sm font-bold"
                                                        onClick={() => handleDeleteComment(reply.id)}
                                                    >
                                                        X
                                                    </button>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                ))}

                        </div>
                    ))
                ) : (
                    <p className="text-gray-500">댓글이 없습니다. 첫 댓글을 작성해보세요!</p>
                )}
            </div>

            <div className="new-comment mt-6">
    <textarea
        value={newComment}
        onChange={(e) => {
            const input = e.target.value;
            setNewComment(input.length <= 50 ? input : input.slice(0, 50));
        }}
        placeholder="타인의 권리를 침해하거나 명예를 훼손하는 댓글은 운영원칙 및 관련 법률에 제재를 받을 수 있습니다. (최대 50자)"
        className="w-full border rounded-md px-3 py-2"
    />
                <button
                    onClick={handleCommentSubmit}
                    className="mt-2 bg-green-500 text-white px-4 py-2 rounded-md"
                >
                    댓글 작성
                </button>
            </div>


            {/* 목록으로 버튼 */}
            <div className="text-center" style={{marginTop: "2rem"}}> {/* 상단 간격을 2rem으로 설정 */}
                <button
                    onClick={() => navigate("/board")}
                    className="w-full bg-green-500 text-white py-2 rounded-md hover:bg-green-600 transition"
                    style={{backgroundColor: "#25E2B6"}}
                >
                    목록으로
                </button>
            </div>

        </div>
    );
};

export default BoardDetail;
