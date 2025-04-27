import "@css/components/commitsModal";
import React, { useState } from 'react';
import { Button, Modal } from 'reactstrap';
import CounterChanges from "./CounterChanges";
import DiffViewer from './DiffViewer';

export default function CommitsModal({ isOpen, onClose, changes }) {
  const [selectedCommit, setSelectedCommit] = useState(null);

  // Obtener los commits únicos
  const allCommits = changes
    .flatMap(change => change.fileChangeDetails.map(detail => ({
        commitSha: detail.commitSha,
        filePath: change.filePath,
        patch: detail.patch,
        additions: detail.additions,
        deletions: detail.deletions,
      }))
    );

  console.log(allCommits)

  const handleCommitClick = (commitSha) => {
    // Filtra todos los cambios relacionados con el commit seleccionado
    const fileChanges = allCommits.filter(fc => fc.commitSha === commitSha);
    setSelectedCommit({ commitSha, fileChanges });
  };

  const closeDiffViewer = () => setSelectedCommit(null);

  return (
    <>
      <Modal isOpen={isOpen} onClose={onClose}>
        <div className='modal-content-commits'>
          <div className="relative">
          <div style={{display: "flex", justifyContent: "end"}}>
            <Button
              className="absolute top-2 right-2 text-2xl font-bold text-gray-600 hover:text-gray-800"
              onClick={onClose}
            >
              ×
            </Button>
          </div>
            <h2 className="text-xl font-semibold mb-4">Lista de Commits</h2>
            <div className="space-y-2">
              {[...new Set(allCommits.map(fc =>{ 
                  return {commitSha: fc.commitSha, additions: fc.additions, deletions: fc.deletions}
                }))].map(({commitSha, additions, deletions}) => (
                <div
                  key={commitSha}
                  className="cursor-pointer text-blue-600 hover:underline inline-flex items-center px-3 py-1 border border-gray-400 rounded-full bg-gray-100"
                  onClick={() => handleCommitClick(commitSha)}
                >
                  <div className="items-column" style={{justifyContent: "space-between"}}>
                    <span className="text-sm">{commitSha.substring(0, 7)}</span>
                    <CounterChanges  additions={additions} deletions={deletions}/>
                  </div>                 
                </div>
              ))}
            </div>
          </div>
        </div>
      </Modal>

      {selectedCommit && (
        <DiffViewer
          isOpen={true}
          fileChanges={selectedCommit.fileChanges}
          onClose={closeDiffViewer}
        />
      )}
    </>
  );
}
