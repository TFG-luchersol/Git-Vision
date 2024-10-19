import React, { useEffect, useState } from 'react';
import { GoIssueClosed, GoIssueOpened } from "react-icons/go";
import { useParams } from 'react-router-dom';
import {Input} from 'reactstrap';
import { IoCopyOutline } from "react-icons/io5";
import tokenService from '../../../services/token.service';
import './issue.css';

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

    console.log(issue)
    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <div className='issue'>
                <div>
                    <span style={{ marginRight: 10 }}>
                        {issue.state === "CLOSED" ?
                            <GoIssueClosed color='purple' /> :
                            <GoIssueOpened color='green' />
                        }
                    </span>
                    <span className="title">{issue.title}</span>
                </div>
                <div>
                    <span className="number">#{issue.number}</span>
                </div>
            </div>
            <ul className='commits-issue-container'>
                {issue.commits?.map((commit, index) =>
                    <li className="commit-row" key={index}>
                        <div>
                            {commit.message}
                        </div>
                        <div style={{display:"flex", flexDirection:"row", alignItems: "center"}}>
                            <Input style={{maxWidth: 300}} value={commit.sha} readOnly/>
                            <IoCopyOutline onClick={() => navigator.clipboard.writeText(commit.sha)} style={{fontSize: 20, marginLeft: 10, cursor:"pointer"}}/>
                        </div>
                    </li>
                )}
            </ul>   
        </div>

    );
}