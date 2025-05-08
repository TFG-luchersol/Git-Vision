import "@css/components/folderTabs.css";
import React, { useState } from 'react';
import BackButton from "./BackButton";

// {'file1': <div>Contenido 1</div>, 'file2': <div>Contenido 2</div>, 'file3': <div>Contenido 3</div>}
export default function FolderTabs({hasBackButton=true, sections={}, style}) {
  const [activeTab, setActiveTab] = useState(null);

  useState(() => {
    setActiveTab(Object.keys(sections)[0])
  }, [])

  const classNameTab = (tab) => `tab ${activeTab === tab ? 'active' : ''}`

  return (Object.keys(sections).length > 0 &&
    <div className="folder-container" style={{...style}}>
      <div className="tab-header">
        {hasBackButton && <BackButton className="tab active" />}
        {
          Object.keys(sections).map((tag) => (
            <button
              onClick={() => setActiveTab(tag)}
              className={classNameTab(tag)}>
              {tag}
            </button>
          ))
        }
      </div>

      <div className="tab-content">
          {Object.entries(sections).map(([key, Component]) => (
            <div
              key={key}
              style={{ display: activeTab === key ? 'block' : 'none' }}
            >
              {Component}
            </div>
          ))}
        </div>
    </div>
  );
}

