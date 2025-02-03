import React from 'react';
import FolderTabs from '../../../components/FolderTabs';
import FileContent from './sections/FileContent';
import FileContribution from './sections/FileContribution';

export default function File() {

    const sections = {
        "Content": <FileContent />,
        "Contributions": <FileContribution />
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
        </div>
    );
}