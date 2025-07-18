import CounterChanges from '@components/CounterChanges.jsx';
import FolderTabs from '@components/FolderTabs.jsx';
import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/commits/commit";
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

export default function Commit() {
    const { showMessage } = useNotification();
    const { owner, repo, sha } = useParams();

    const [commit, setCommit] = useState([])

    useEffect(() => {
        getCommit()
    }, [])

    const getCommit = async () => {
        try {
            const repositorName = `${owner}/${repo}`;
            const response = await fetchBackend(`/api/v1/commits/${repositorName}/${sha}`)
            const commit = await getBody(response)
            setCommit(commit)
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    const sections = {
        "Cuerpo":(<p>{commit.body || "No tiene cuerpo"}</p>),
        "Archivos":
        (<ul className="file-container">
                {commit.files?.map((file, index) => {
                    return (<li id={index} className='file-row'>
                        {file.fileName}
                        <CounterChanges additions={file.linesAdded} deletions={file.linesDeleted}/>
                    </li>);
                })}
                </ul>),
        "Issues": (<ul>
            {commit.issues?.map((issue, index) => {
                return (<li id={index} className='file-row' onClick={() => window.location.href = `/repository/${owner}/${repo}/issues/${issue.number}` }>
                    <div>
                        {issue.title}
                    </div>
                    <div>
                        #{issue.number}
                    </div>
                </li>);
            })}
        </ul>)
    }

    return (
        <div style={{display: "flex", justifyContent:"center", flexDirection: "column", position:"absolute", top: "15%", width: "87%"}}>
            <div className='commit-message'>
                {commit.message}
            </div>
            <div style={{marginTop: "10px"}}>
                <FolderTabs sections={sections}/>
            </div>
        </div>
    );
}
