import React from "react";
import DaumPostcode from "react-daum-postcode";

const AddressModal = ({ isOpen, onClose, onComplete }) => {
    if (!isOpen) return null; // 팝업이 열리지 않으면 렌더링하지 않음

    return (
        <div
            style={{
                position: "fixed",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                backgroundColor: "rgba(0, 0, 0, 0.5)", // 어두운 배경
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                zIndex: 1000,
            }}
        >
            <div
                style={{
                    backgroundColor: "white",
                    border: "1px solid #ccc",
                    borderRadius: "8px",
                    boxShadow: "0 4px 10px rgba(0, 0, 0, 0.3)",
                    width: "520px",
                    height: "600px",
                    display: "flex",
                    flexDirection: "column",
                    position: "relative",
                }}
            >
                {/* 헤더 */}
                <div
                    style={{
                        backgroundColor: "#f1f1f1",
                        padding: "10px 20px",
                        borderBottom: "1px solid #ccc",
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        borderTopLeftRadius: "8px",
                        borderTopRightRadius: "8px",
                    }}
                >
                    <span style={{ fontSize: "16px", fontWeight: "bold" }}>주소 검색</span>
                    <button
                        onClick={onClose}
                        style={{
                            backgroundColor: "transparent",
                            border: "none",
                            fontSize: "16px",
                            cursor: "pointer",
                            color: "#333",
                        }}
                    >
                        ✕
                    </button>
                </div>

                {/* DaumPostcode */}
                <div style={{ flex: 1, overflow: "hidden" }}>
                    <DaumPostcode
                        onComplete={onComplete}
                        style={{ width: "100%", height: "100%" }}
                    />
                </div>
            </div>
        </div>
    );
};

export default AddressModal;