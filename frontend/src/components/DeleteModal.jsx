import React from 'react';
import { Button, Modal, ModalBody, ModalHeader } from 'reactstrap';

export default function DeleteModal({text="¿Estás seguro?", subtitle = "", handleDelete, isOpen, onClose}) {
    return (
      <Modal style={{position:'absolute', top: "50%", left: "50%", transform: "translate(-50%, -50%)", padding: "20px"}} isOpen={isOpen}>
        <ModalHeader>
          {text}
          {subtitle && 
          (
            <div style={{ fontSize: "0.8rem", color: "#6c757d", marginTop: "4px" }}>
                {subtitle}
            </div>
          )}
        </ModalHeader>
        <ModalBody style={{display: "flex", justifyContent: "space-around"}}>
          <Button color="primary" onClick={handleDelete}>SI</Button>
          <Button color="danger" onClick={onClose}>NO</Button>
        </ModalBody>
      </Modal>
    );
}
