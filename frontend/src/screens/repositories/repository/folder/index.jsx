import Contributions from '@components/Contributions';
import FolderTabs from '@components/FolderTabs';
import React from 'react';
import { useParams } from 'react-router-dom';
import FolderContent from './sections/FolderContent';


export default function Folder() {
    
    const { owner, repo, "*": path } = useParams();
    
    const sections = {
        "Files": <FolderContent />,
        "Contributions": <Contributions owner={owner} repo={repo} path={path + "&isFolder=true"} />,
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
        </div>
    );
}