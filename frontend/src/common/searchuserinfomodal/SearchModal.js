import React from "react";

const SearchModal = ({ isOpen, onClose, children }) => {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <button className="close-button" onClick={onClose}>
                    X
                </button>
                {children}
            </div>
            <style jsx>{`
                .modal-overlay {
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 0.6);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 1000; /* 모달이 최상단에 위치하도록 */
                }
                .modal-content {
                    background: white;
                    padding: 20px;
                    border-radius: 10px;
                    max-width: 500px;
                    width: 80%;
                    position: relative;
                    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
                    animation: fadeIn 0.3s ease; /* 모달 등장 애니메이션 */
                }
                @keyframes fadeIn {
                    from {
                        opacity: 0;
                        transform: translateY(-20px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }
                .close-button {
                    position: absolute;
                    top: 10px;
                    right: 10px;
                    background: none;
                    border: none;
                    font-size: 16px;
                    cursor: pointer;
                }
            `}</style>
        </div>
    );
};

export default SearchModal;
