import React from "react";
import { Link } from "react-router-dom";

function BoardMain() {
    return (
        <div>
            <h3>
                여기는 BoardMain 입니다
                <h1>
                    <Link to="/boardlist">BoardList로 이동하기</Link>
                </h1>
            </h3>
        </div>
    );
}

export default BoardMain;
