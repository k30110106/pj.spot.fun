import React from "react";

const AlertModal = ({ isOpen, message, onClose }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div className="bg-white p-6 rounded-lg text-center shadow-lg">
                <p>{message}</p>
                <button
                    onClick={onClose}
                    className="mt-4 px-4 py-2 bg-custom-cyan rounded hover:bg-emerald-500"
                >
                    확인
                </button>
            </div>
        </div>
    );
};

export default AlertModal;