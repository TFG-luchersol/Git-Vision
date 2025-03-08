import React from 'react';
import { Modal } from 'reactstrap';
import '../static/css/components/loadingModal.css';

export default function LoadingModal({ isLoading, message="Cargando..." }) {

  return (
  <Modal isOpen={isLoading} className="modal-overlay">
      <div className="modal-content">
          <div className="loader"></div>
          <p style={{marginTop: 30}}>{message}</p>
        </div>
    </Modal>
  );
};

