import FolderTabs from '@components/FolderTabs.jsx';
import React from 'react';
import FilesChangesByUser from './sections/FilesChangesByUser';

export default function Contributor() {

    const sections = {
        "Files": <FilesChangesByUser />
    }

    return (
        <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
    );
}

