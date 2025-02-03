import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import TreeFiles from '../../../../components/TreeFiles';
import tokenService from '../../../../services/token.service';
import getBody from '../../../../util/getBody';

export default function FolderContent() {
    const { owner, repo, "*": path } = useParams();
    const { username } = tokenService.getUser();
    
    const [files, setFiles] = useState([])

    useEffect(() => {
        getFiles();
    }, [])

    async function getFiles() {
        try {
            const response = await fetch(`/api/v1/files/repository/${owner}/${repo}/tree/files?login=${username}&path=${path}`)
            const json = await response.json();
            const { tree } = getBody(json);
            setFiles(tree.children);
        } catch (error) {
            alert(error)
        }
    }

    console.log(files)
    return (
        <>
        {files.map(d => <TreeFiles root={ d}/>)}
        </>
    );
}