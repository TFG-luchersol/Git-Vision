import CounterChanges from '@components/CounterChanges.jsx';
import FolderTabs from '@components/FolderTabs.jsx';
import { useNotification } from '@context/NotificationContext';
import '@css/repositories/repository/issues/issue';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { GoIssueClosed, GoIssueOpened } from "react-icons/go";
import { IoCopyOutline } from "react-icons/io5";
import { useParams } from 'react-router-dom';
import { Input } from 'reactstrap';

export default function Issue() {
    const { showMessage } = useNotification();
    const { owner, repo, issueNumber } = useParams();

    const [issue, setIssue] = useState({});
    const [commits, setCommits] = useState([])
    const [files, setFiles] = useState([])
    const [changesByUser, setChangesByUser] = useState({})
    const [assignees, setAssignees] = useState([])

    useEffect(() => {
        getIssue()
    }, [])

    const getIssue = async () => {
        try {
            const newIssues = await fetchBackend(`/api/v1/issues/${owner}/${repo}/${issueNumber}`)
            const {issue, commits, files, changesByUser, assignees} = await getBody(newIssues)
            setIssue(issue)
            setCommits(commits)
            setFiles(files)
            setChangesByUser(changesByUser)
            setAssignees(assignees)
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    const sections = {
        "Cuerpo": (<p>{issue.body || "No tiene cuerpo"}</p>),
        "Commits": (<ul className='commit-container'>
            {commits?.map((commit, index) =>
                <li className="commit-row" key={index}>
                    <div>
                        {commit.message}
                    </div>
                    <div style={{ display: "flex", flexDirection: "row", alignItems: "center" }}>
                        <Input style={{ maxWidth: 300 }} value={commit.sha} readOnly />
                        <IoCopyOutline onClick={() => navigator.clipboard.writeText(commit.sha)} style={{ fontSize: 20, marginLeft: 10, cursor: "pointer" }} />
                    </div>
                </li>
            )}
        </ul>),
        "Archivos": (<ul className='commit-container'>
            {files?.sort().map((file, index) =>
                <li className="commit-row" key={index}>
                    <div>
                        {file}
                    </div>
                </li>
            )}
        </ul>),
        "Cambios por Usuario": (<ul className='commit-container'>
            {Object.entries(changesByUser).map((entry, index) =>
                <li className="commit-row" key={index}>
                    <div>
                        {entry[0]}
                    </div>
                    <CounterChanges additions={entry[1].additions} deletions={entry[1].deletions}/>
                </li>
            )}
        </ul>),
        "Asignados": (<ul className='commit-container'>
            {assignees.map((assignee, index) => 
                <li className="commit-row" key={index}>
                    <img alt={assignee.username} src={assignee.avatarUrl}
                         style={{borderRadius: "50%", width: "100px", height: "100px"}}/>
                    <p>{assignee.username}</p>
                </li>
            )}
        </ul>)
    }

    return (
        <div style={{ display: "flex", justifyContent: "center", flexDirection: "column", position: "absolute", top: "15%", width: "87%" }} className='issue-container'>

            <span className="issue-message">
                <div style={{ display: "flex", flexDirection: "row" }}>
                    <span style={{ marginRight: 10 }}>
                        {issue.state?.toUpperCase() === "CLOSED" ?
                            <GoIssueClosed color='purple' /> :
                            <GoIssueOpened color='green' />
                        }
                    </span>
                    <p style={{ marginTop: 1 }}>{issue.title}</p>
                </div>
                <span className="number">#{issue.number}</span>
            </span>

            <div style={{ marginTop: "10px" }}>
                <FolderTabs sections={sections} />
            </div>
        </div>
    );
}
