import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/issues";
import fetchWithToken from '@utils/fetchWithToken.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { GoIssueClosed, GoIssueOpened } from 'react-icons/go';
import { useParams } from 'react-router-dom';
import { Button, Input } from 'reactstrap';

export default function Issues() {
    const { showMessage } = useNotification();
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
            const newIssues = await fetchWithToken(`/api/v1/issues/${owner}/${repo}?page=${page}`)
            const issues = await getBody(newIssues)
            if(issues.length > 0){
                setIssues(issues)
                setPage(page)
            } 
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    const nextPage = () => getIssues(page + 1)
    const previousPage = () => getIssues(page <= 1 ? 1 : page - 1)

    return (
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
    );
}
