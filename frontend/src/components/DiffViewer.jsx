import "@css/components/diffViewer";
import React from 'react';
import { Button, Modal } from 'reactstrap';

export default function DiffViewer({ isOpen, onClose, fileChanges }) {
  return (
    <Modal isOpen={isOpen} toggle={onClose} size="xl">
      <div className="modal-content-diff-viewer">
        {/* Cerrar el modal */}
        <div style={{ display: "flex", justifyContent: "flex-end" }}>
          <Button className="btn-close" onClick={onClose}>×</Button>
        </div>

        {/* Contenido del diff */}
        <div className="diff-container">
          {fileChanges.map(({ filePath, commitSha, patch }, i) => (
            <div key={i} className="diff-block">
              <h3>{filePath} — <span style={{ color: '#666' }}>{commitSha.substring(0, 7)}</span></h3>

              <pre>
                {patch.split('\n').map((line, idx) => {
                  let lineClass = "diff-line";
                  if (line.startsWith('+')) lineClass += " line-added";
                  else if (line.startsWith('-')) lineClass += " line-removed";
                  else if (line.startsWith('@@')) lineClass += " line-hunk";

                  return (
                    <div key={idx} className={lineClass}>
                      {line}
                    </div>
                  );
                })}
              </pre>
            </div>
          ))}
        </div>
      </div>
    </Modal>
  );
}
