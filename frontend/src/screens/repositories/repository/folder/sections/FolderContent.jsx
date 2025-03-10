import TreeFiles from '@components/TreeFiles.jsx';
import '@css/repositories/repository/folder/sections/folderContent.css';
import fetchWithToken from '@utils/fetchWithToken.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

export default function FolderContent() {
    // console.log(styles)
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
        <div className='folder-content'>
            {files.map(file => <TreeFiles root={file}/>)}
        </div>
    );
}