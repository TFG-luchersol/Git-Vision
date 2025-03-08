import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CounterChanges from '../../../../components/CounterChanges.jsx';
import FolderTabs from '../../../../components/FolderTabs.jsx';
import "../../../../static/css/repositories/repository/commits/commit/commit.css";
import fetchWithToken from '../../../../util/fetchWithToken.ts';
import getBody from '../../../../util/getBody.ts';

export default function Commit() {
    const { owner, repo, sha } = useParams();

    const [commit, setCommit] = useState([])

    useEffect(() => {
        getCommit()
    }, [])

    const getCommit = async () => {
        try {
            const repositorName = `${owner}/${repo}`;
            const newCommit = await fetchWithToken(`/api/v1/commits/${repositorName}/${sha}`)
            const {commit} = await getBody(newCommit)
            setCommit(commit)
        } catch (e) {
            alert(e.message)
        }
    }

    const sections = {
        "Body":(<p>{commit.body || "No tiene cuerpo"}</p>),
        "Files":
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
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc"}}>
            <div style={{display: "flex", justifyContent:"center", flexDirection: "column", position:"absolute", top: "15%", width: "87%"}}>
                <div className='commit-message'>
                    {commit.message}
                </div>
                <div style={{marginTop: "10px"}}>
                    <FolderTabs sections={sections}/>
                </div>
            </div>
        </div>
    );
}