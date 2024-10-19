import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Input } from 'reactstrap';
import tokenService from '../../../services/token.service';
import './issues.css';

export default function Commits() {
    const { username } = tokenService.getUser();
    const { owner, repo } = useParams();

    const [issues, setIssues] = useState([])
    const [filterText, setFilterText] = useState("")
    const [page, setPage] = useState(1)

    useEffect(() => {
        getIssues()
    }, [])

    const getIssues = async () => {
        try {
            const repositorName = `${owner}/${repo}`;
            const newIssues = await fetch(`/api/v1/issues/${repositorName}?login=${username}&page=${page}`)
            const json = await newIssues.json()
            console.log(json)
            setIssues(json.information.information.issues)
        } catch (e) {

        }
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc"}}>
            <div style={{display: "flex", flexDirection: "column", position:"absolute", top: "15%", width: "87%"}}>
                <Input value={filterText}
                    onChange={(e) => setFilterText(e.target.value)} 
                    style={{position: "relative", left: "6%"}}/>
                <ul className='commits-container'>
                    {issues.map((commit, index) => 
                        <li className="commit-row" key={index} 
                            onClick={() => window.location.href += '/'+commit.sha}>
                            <div>
                                <span className="title">{commit.message}</span>
                            </div>
                        </li>
                    )}
                    <div style={{position: "relative", top:20, display:"flex", justifyContent:"center"}}>
                    </div>
                </ul>
            </div>
        </div>
    );
}