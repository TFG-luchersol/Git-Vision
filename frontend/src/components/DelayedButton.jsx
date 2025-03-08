import React from "react";

import "./css/delayedButton.css";

import '../static/css/delayedButton/delayedButton.css';

export default function DelayedButton({ onClick, disabled, text = "" }) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className="button"
      style={{ marginLeft: 100 }}
    >
      {disabled ? <div className="spinner" /> : text}
    </button>
  );
}
