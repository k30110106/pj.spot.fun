import React from "react";

const AlertModal = ({ isOpen, message, onClose }) => {
    if (!isOpen) return null;

    return (
        <div className="alert-modal">
            <div className="alert-modal-content">
                <p>{message}</p>
                <button onClick={onClose}>확인</button>
            </div>
            <style jsx>{`
                .alert-modal {
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 0.5);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 1000;
                }
                .alert-modal-content {
                    background: white;
                    padding: 20px;
                    border-radius: 8px;
                    text-align: center;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                }
                button {
                    margin-top: 10px;
                    padding: 10px 20px;
                    background: #007bff;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                }
                button:hover {
                    background: #0056b3;
                }
            `}</style>
        </div>
    );
};

export default AlertModal;
