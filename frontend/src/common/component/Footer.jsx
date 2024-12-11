import React, { useEffect, useState } from 'react';

const Footer = () => {
    const [isVisible, setIsVisible] = useState(false); // 푸터 표시 상태 관리

    useEffect(() => {
        const target = document.getElementById('scrollTarget'); // 관찰 대상 요소

        const observer = new IntersectionObserver(
            ([entry]) => {
                setIsVisible(entry.isIntersecting); // 대상이 뷰포트에 들어오면 true, 아니면 false
            },
            { threshold: 0.5 } // 요소가 100% 뷰포트에 들어왔을 때만 트리거
        );

        if (target) {
            observer.observe(target);
        }

        return () => {
            if (target) {
                observer.unobserve(target);
            }
        };
    }, []);

    return (
        <>
            <div id="scrollTarget" style={{ height: '3vh' }}> {/* 테스트용 긴 영역 */}
            </div>
            {isVisible && ( // 푸터가 보일 때만 렌더링
                <footer className="bg-gray-800 text-white p-4 w-3/5 text-center fixed bottom-0">
                    <div className="container mx-auto">
                        <p>&copy; 2024 FunSpot</p>
                    </div>
                </footer>
            )}
        </>
    );
};

export default Footer;
