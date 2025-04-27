import React from "react";

import '@css/components/delayedButton.css';

export default function DelayedButton({style = {}, onClick, disabled, text = "" }) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className="button"
      style={{...style }}
    >
      {disabled ? <div className="spinner" /> : text}
    </button>
  );
}
