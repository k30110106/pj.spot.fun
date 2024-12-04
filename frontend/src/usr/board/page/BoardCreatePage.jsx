import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function BoardCreatePage() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        // 서버로 게시글 데이터 전송
        fetch("/api/boards", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ title, content }),
        })
            .then((response) => response.json())
            .then(() => navigate("/boardlist"))
            .catch((error) => console.error("Error creating board:", error));
    };

    return (
        <div>
            <h1>게시글 작성</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>제목:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                </div>
                <div>
                    <label>내용:</label>
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />
                </div>
                <button type="submit">작성</button>
            </form>
        </div>
    );
}

export default BoardCreatePage;
