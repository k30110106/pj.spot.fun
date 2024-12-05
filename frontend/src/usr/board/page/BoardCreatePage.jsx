import React, { useState } from "react";

function BoardCreatePage() {
    const [title, setTitle] = useState("");
    const [body, setBody] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        const post = { title, body };

        fetch('https://jsonplaceholder.typicode.com/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(post),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // 성공 시 리디렉션 등 추가 작업
            });
    };

    return (
        <div>
            <h1>게시글 작성</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>제목:</label>
                    <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
                </div>
                <div>
                    <label>내용:</label>
                    <textarea value={body} onChange={(e) => setBody(e.target.value)} />
                </div>
                <button type="submit">작성하기</button>
            </form>
        </div>
    );
}

export default BoardCreatePage;
