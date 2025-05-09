import React from "react";
import { FiArrowLeft } from "react-icons/fi";
import { useNavigate } from "react-router-dom";

export default function BackButton({className = "", size = 24, color = "black", style = {}, backRoute }) {
  const navigate = useNavigate();

  return (
    <button
    className={className}
    onClick={() => navigate(backRoute ?? -1)}
    >
        <FiArrowLeft
        
        size={size}
        color={color}
        style={{
            cursor: "pointer",
            ...style,
        }}
        title="Volver"
        />
    </button>
  );
};
