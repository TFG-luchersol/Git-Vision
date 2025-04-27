import TreeFiles from '@components/TreeFiles.jsx';
import { useNotification } from '@context/NotificationContext';
import '@css/repositories/repository/folder/sections/folderContent.css';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

export default function FolderContent() {
    const { showMessage } = useNotification();
    const { owner, repo, "*": path } = useParams();
    
    const [files, setFiles] = useState([])

    useEffect(() => {
        getFiles();
    }, [])

    async function getFiles() {
        try {
            const response = await fetchBackend(`/api/v1/files/repository/${owner}/${repo}/tree/files?path=${path}`)
            const tree = await getBody(response);
            setFiles(tree.children);
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    return (
        <div className='folder-content'>
            {files.map(file => <TreeFiles root={file}/>)}
        </div>
    );
}
