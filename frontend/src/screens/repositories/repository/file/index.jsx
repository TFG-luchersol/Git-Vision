import Contributions from '@components/Contributions';
import FolderTabs from '@components/FolderTabs';
import React from 'react';
import { useParams } from 'react-router-dom';
import FileContent from './sections/FileContent';

export default function File() {

    const { owner, repo, "*": path } = useParams();

    const sections = {
        "Contenido": <FileContent />,
        "Contribuciones": <Contributions owner={owner} repo={repo} path={path} />,
    }

    return (
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
    );
}
