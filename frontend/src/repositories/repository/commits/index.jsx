import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, Input } from 'reactstrap';
import "../../../static/css/repositories/repository/commits";
import fetchWithToken from '../../../util/fetchWithToken.ts';
import getBody from '../../../util/getBody.ts';

export default function Commits() {
    const { owner, repo } = useParams();

    const [commits, setCommits] = useState([])
    const [filterText, setFilterText] = useState("")
    const [page, setPage] = useState(1)

    useEffect(() => {
        getCommits()
    }, [page])

    const getCommits = async () => {
        try {
            const repositorName = `${owner}/${repo}`;
            const newCommits = await fetchWithToken(`/api/v1/commits/${repositorName}?page=${page}`)
            const {commits} = await getBody(newCommits)
            setCommits(commits)
        } catch (e) {
            alert(e)
        }
    }

    const nextPage = () => setPage(page + 1)
    const previousPage = () => setPage(page <= 1 ? 1 : page - 1)

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc"}}>
            <div style={{display: "flex", flexDirection: "column", position:"absolute", top: "15%", width: "87%"}}>
                <Input value={filterText}
                    onChange={(e) => setFilterText(e.target.value)} 
                    style={{position: "relative", left: "6%"}}/>
                <ul className='commits-container'>
                    {commits.map((commit, index) => 
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
                <div style={{display:"flex",justifyContent:"center"}}>
                    <Button onClick={previousPage}>Anterior</Button>
                    <div style={{padding:10, paddingInline: 20}}>{page}</div>
                    <Button onClick={nextPage}>Siguiente</Button>
                </div>
            </div>
        </div>
    );
}