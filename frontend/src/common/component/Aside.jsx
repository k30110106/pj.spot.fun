import React from 'react';
import {Link, useNavigate} from "react-router-dom";

const Aside = () => {
    const navigate = useNavigate();
    return (
        <aside id="aside-component">
            사이드 메뉴영역
            <ul>
                <li>
                    <Link to={"/"}>메인</Link>
                </li>
                <li>
                    <Link to={"/login"}>로그인</Link>
                </li>
                <li>
                    <Link to={"/mypage"}>마이페이지</Link>
                </li>
                <li>
                    <Link to={"/signup"}>회원가입</Link>
                </li>
                <li>
                    <Link to={"/feed"}>피드</Link>
                </li>
                {/*<li>
                    <button type={"button"}
                            onClick={() => {
                                localStorage.removeItem("access_token");
                                navigate({pathname: "/"}, {replace: true});
                            }}>로그아웃</button>
                </li>*/}
            </ul>
        </aside>
    );
};

export default Aside;