import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

function BoardDetailPage() {
    const { id } = useParams();
    const [post, setPost] = useState(null);

    useEffect(() => {
        fetch(`https://jsonplaceholder.typicode.com/posts/${id}`)
            .then(response => response.json())
            .then(data => setPost(data));
    }, [id]);

    if (!post) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <p>글 번호 : {post.id}</p>
            <h1>제목 : {post.title}</h1>
            
            <p>작성자 : {post.userId}</p>
            <p>내용 : {post.body}</p>
        </div>
    );
}

export default BoardDetailPage;
