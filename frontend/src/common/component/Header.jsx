import React from 'react';

const Header = () => {
    return (
        <header className="bg-white text-black p-4 mb-4 shadow-lg">
            <div className="container mx-auto flex justify-between items-center">
                <a href="/"><h1 className="text-3xl font-bold">FunSpot</h1></a>
                <div>
                    <a href="/login" className="mr-2">로그인</a>
                    <span className='p-1 cursor-default'>|</span>
                    <a href="/sign-up"> 회원가입</a>
                </div>
            </div>
        </header>
    );
};

export default Header;