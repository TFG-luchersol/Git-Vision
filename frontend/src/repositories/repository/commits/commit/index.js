import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import tokenService from '../../../../services/token.service';
import CounterChanges from '../../../../components/CounterChanges';
import FolderTabs from '../../../../components/FolderTabs';
import './commit.css';
import getBody from '../../../../util/getBody';

export default function Commit() {
    const { username } = tokenService.getUser();
    const { owner, repo, sha } = useParams();

    const [commit, setCommit] = useState([])

    useEffect(() => {
        getCommit()
    }, [])

    const getCommit = async () => {
        try {
            const repositorName = `${owner}/${repo}`;
            const newCommit = await fetch(`/api/v1/commits/${repositorName}/${sha}?login=${username}`)
            const json = await newCommit.json()
            const {commit} = getBody(json)
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