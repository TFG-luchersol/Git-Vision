import React, { createContext, useContext, useEffect, useState } from "react";
import Notification from "../components/Notification";

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
  const [notification, setNotification] = useState(null);

  const showNotification = (newNotification) => {
    const id = Math.random().toString(36).substring(7);
    const notif = { ...newNotification, id };
    setNotification(notif);
    localStorage.setItem("cross-tab-notification", JSON.stringify({ ...notif, timestamp: Date.now() }));
  };

  const hideNotification = () => {
    setNotification(null);
  };

  // Listen for cross-tab notifications (opcional)
  useEffect(() => {
    const handleStorage = (event) => {
      if (event.key === "cross-tab-notification" && event.newValue) {
        const notif = JSON.parse(event.newValue);
        setNotification(notif);
      }
    };

    window.addEventListener("storage", handleStorage);
    return () => window.removeEventListener("storage", handleStorage);
  }, []);

  return (
    <NotificationContext.Provider value={{ showNotification, showMessage: showNotification }}>
      {children}
      {notification && (
        <Notification
          key={notification.id}
          message={notification.message}
          type={notification.type}
          duration={notification.duration}
          onHide={hideNotification}
        />
      )}
    </NotificationContext.Provider>
  );
};

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error("useNotification must be used within a NotificationProvider");
  }
  return context;
};
