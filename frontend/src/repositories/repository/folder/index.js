import React from 'react';
import FolderTabs from '../../../components/FolderTabs.js';
import FolderContent from './sections/FolderContent.js';
import FolderContributions from './sections/FolderContributions.js';


export default function Folder() {

    const sections = {
        "Files": <FolderContent />,
        "Contributions": <FolderContributions />,
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
        </div>
    );
}