import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

function BoardDetailPage() {
    const { id } = useParams();
    const [board, setBoard] = useState(null);

    useEffect(() => {
        fetch(`/api/boards/${id}`)
            .then((response) => response.json())
            .then((data) => setBoard(data))
            .catch((error) => console.error("Error fetching board:", error));
    }, [id]);

    if (!board) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>{board.title}</h1>
            <p>{board.content}</p>
        </div>
    );
}

export default BoardDetailPage;
