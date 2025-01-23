import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, Input } from 'reactstrap';
import tokenService from '../../../services/token.service';
import './issues.css';
import { GoIssueClosed, GoIssueOpened } from 'react-icons/go';

export default function Issues() {
    const { username } = tokenService.getUser();
    const { owner, repo } = useParams();

    const [issues, setIssues] = useState([])
    const [filterText, setFilterText] = useState("")
    const [page, setPage] = useState(1)

    useEffect(() => {
        getIssues(page)
    }, [])

    const getIssues = async (page) => {
        if(page < 1) return;
        try {
            const repositorName = `${owner}/${repo}`;
            const newIssues = await fetch(`/api/v1/issues/${repositorName}?login=${username}&page=${page}`)
            const json = await newIssues.json()
            if(json.information.information.issues.length > 0){
                setIssues(json.information.information.issues)
                setPage(page)
            } 
        } catch (e) {

        }
    }

    const nextPage = () => getIssues(page + 1)
    const previousPage = () => getIssues(page <= 1 ? 1 : page - 1)

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <div style={{ display: "flex", flexDirection: "column", position: "absolute", top: "15%", width: "87%" }}>
                {
                    issues.length === 0 ? <p style={{margin: "5%"} }> NO HAY ISSUES</p> :
                        <>
                            <Input value={filterText}
                                onChange={(e) => setFilterText(e.target.value)}
                                style={{ position: "relative", left: "6%" }} />
                            <ul className='commits-container'>
                                {issues.map((issue, index) =>
                                    <li className="commit-row" key={index}
                                        onClick={() => window.location.href += '/' + issue.number}>
                                        <div style={{display:"flex", alignItems:"column"}}>
                                            <div style={{marginRight:"10px"}}>
                                                {issue.state.toUpperCase() === "CLOSED" ?
                                                    <GoIssueClosed color='purple' /> :
                                                    <GoIssueOpened color='green' />
                                                }
                                            </div>
                                            <span className="title">{issue.title}</span>
                                        </div>
                                        <div>
                                            <span className="number">#{issue.number}</span>
                                        </div>
                                    </li>
                                )}
                                <div style={{ position: "relative", top: 20, display: "flex", justifyContent: "center" }}>
                                </div>
                            </ul>
                            <div style={{display:"flex",justifyContent:"center"}}>
                                <Button onClick={previousPage}>Anterior</Button>
                                <div style={{padding:10, paddingInline: 20}}>{page}</div>
                                <Button onClick={nextPage}>Siguiente</Button>
                            </div>
                        </>}
            </div>
        </div>
    );
}