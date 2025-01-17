import React, { useState } from 'react';
import "./folderTabs.css";

// {'file1': <div>Contenido 1</div>, 'file2': <div>Contenido 2</div>, 'file3': <div>Contenido 3</div>}
export default function FolderTabs({sections={}, style}) {
  const [activeTab, setActiveTab] = useState(null);

  useState(() => {
    setActiveTab(Object.keys(sections)[0])
  }, [])

  const renderContent = () => sections[activeTab];

  const classNameTab = (tab) => `tab ${activeTab === tab ? 'active' : ''}`

  return (Object.keys(sections).length > 0 &&
    <div className="folder-container" style={{...style}}>
      <div className="tab-header">
        {
          Object.entries(sections).map((entry) => 
            <button
              onClick={() => setActiveTab(entry[0])}
              className={classNameTab(entry[0])}>
              {entry[0]}
            </button>
          )
        }
      </div>

      <div className="tab-content">
        {renderContent()}
      </div>
    </div>
  );
}

