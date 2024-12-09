import React from "react";

const SearchModal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-60 z-50">
      <div className="bg-white p-6 rounded-lg max-w-md w-full relative shadow-lg animate-fadeIn">
        <button
          className="absolute top-2 right-2 bg-transparent border-none text-lg cursor-pointer"
          onClick={onClose}
        >
          X
        </button>
        {children}
      </div>
    </div>
  );
};

export default SearchModal;
