import React from "react";
import DaumPostcode from "react-daum-postcode";

const AddressModal = ({ isOpen, onClose, onComplete }) => {
    if (!isOpen) return null; // 팝업이 열리지 않으면 렌더링하지 않음

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div className="bg-white border border-gray-300 rounded-lg shadow-lg w-[520px] h-[600px] flex flex-col relative">
                {/* 헤더 */}
                <div className="bg-gray-100 p-3 border-b border-gray-300 flex justify-between items-center rounded-t-lg">
                    <span className="text-lg font-bold">주소 검색</span>
                    <button
                        onClick={onClose}
                        className="bg-transparent border-none text-lg cursor-pointer text-gray-700"
                    >
                        ✕
                    </button>
                </div>

                {/* DaumPostcode */}
                <div className="flex-1 overflow-hidden">
                    <DaumPostcode
                        onComplete={onComplete}
                        className="w-full h-full"
                    />
                </div>
            </div>
        </div>
    );
};

export default AddressModal;
