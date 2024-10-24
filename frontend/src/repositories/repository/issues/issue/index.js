import React, { useEffect, useState } from 'react';
import { GoIssueClosed, GoIssueOpened } from "react-icons/go";
import { useParams } from 'react-router-dom';
import { Input } from 'reactstrap';
import { IoCopyOutline } from "react-icons/io5";
import tokenService from '../../../../services/token.service';
import './issue.css';
import FolderTabs from '../../../../components/FolderTabs';

export default function Issue() {
    const { username } = tokenService.getUser();
    const { owner, repo, issueNumber } = useParams();

    const [issue, setIssue] = useState({});

    useEffect(() => {
        getIssue()
    }, [])

    const getIssue = async () => {
        try {
            const id = `${owner}/${repo}`;
            const newIssues = await fetch(`/api/v1/issues/${id}/${issueNumber}?login=${username}`)
            const json = await newIssues.json()
            setIssue(json.information.information.issue)
        } catch (e) {

        }
    }

    const sections = {
        "Body": (<p>{issue.body || "No tiene cuerpo"}</p>),
        "Commits": (<ul className='commit-container'>
            {issue.commits?.map((commit, index) =>
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
        </ul>)
    }

    console.log(issue)
    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>

            <div style={{ display: "flex", justifyContent: "center", flexDirection: "column", position: "absolute", top: "15%", width: "87%" }} className='issue-container'>
                
                <span className="issue-message">
                    <div style={{display:"flex", flexDirection:"row"}}>
                        <span style={{ marginRight: 10 }}>
                            {issue.state?.toUpperCase() === "CLOSED" ?
                                <GoIssueClosed color='purple' /> :
                                <GoIssueOpened color='green' />
                            }
                        </span>
                        <p style={{marginTop:1}}>{issue.title}</p>
                    </div>
                    <span className="number">#{issue.number}</span>
                </span>

                <div style={{ marginTop: "10px" }}>
                    <FolderTabs sections={sections} />
                </div>
            </div>

        </div>

    );
}