import React from "react";
import { Link, Outlet } from "react-router-dom";

function BoardIndexPage() {
    return (
        <div>
            <h3>
                여기는 BoardIndex 입니다.
                <h1>
                    <Link to="list">[BoardList로 이동하기]</Link>
                    <Link to="create">[글쓰기]</Link>
                </h1>
            </h3>
            {/* Outlet: 여기에 하위 페이지가 렌더링됨 */}
            <Outlet />
        </div>
    );
}

export default BoardIndexPage;
