import "@css/components/notification";
import { blue, green, orange, red } from "@mui/material/colors";
import React, { useEffect, useRef } from "react";

const Notification = ({
  message,
  type = "error",
  duration = 2000,
  onHide,
}) => {
  const notificationRef = useRef();

  const colors = {
    success: green.A400,
    error: red[300],
    info: blue[100],
    warning: orange[200]
  };

  useEffect(() => {
    const el = notificationRef.current;
    if (!el) return;

    el.classList.add("show");

    const timer = setTimeout(() => {
      el.classList.remove("show");
      el.classList.add("hide");

      // Espera a que termine la animación antes de ocultar
      el.addEventListener("animationend", onHide, { once: true });
    }, duration);

    return () => clearTimeout(timer);
  }, [message, duration]);

  return (
    <div
      ref={notificationRef}
      className="notification"
      style={{ backgroundColor: colors[type] }}
    >
      {message}
    </div>
  );
};

export default Notification;
