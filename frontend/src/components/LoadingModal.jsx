import '@css/components/loadingModal.css';
import React from 'react';
import { Modal } from 'reactstrap';

export default function LoadingModal({ isLoading, message="Cargando..." }) {

  return (
    <Modal isOpen={isLoading}  className="center-screen">
      <div className="modal-content-loading">
          <div className="loader"></div>
          <p style={{marginTop: 30}}>{message}</p>
        </div>
    </Modal>
  );
};

