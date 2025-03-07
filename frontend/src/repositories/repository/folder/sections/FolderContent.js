import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import TreeFiles from '../../../../components/TreeFiles';
import fetchWithToken from '../../../../util/fetchWithToken.ts';
import getBody from '../../../../util/getBody.ts';

export default function FolderContent() {
    const { owner, repo, "*": path } = useParams();
    
    const [files, setFiles] = useState([])

    useEffect(() => {
        getFiles();
    }, [])

    async function getFiles() {
        try {
            const response = await fetchWithToken(`/api/v1/files/repository/${owner}/${repo}/tree/files?path=${path}`)
            const { tree } = await getBody(response);
            setFiles(tree.children);
        } catch (error) {
            alert(error)
        }
    }

    return (
        <div>
            {files.map(file => <TreeFiles root={file}/>)}
        </div>
    );
}