import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import tokenService from '../../../services/token.service.js';
import FolderTabs from '../../../components/FolderTabs.js';
import CommitsByTime from './stadistics/CommitsByTime.js';

export default function Contributors() {
    const { username } = tokenService.getUser();
    const { owner, repo } = useParams();

    const [contributors, setContributors] = useState([]);

    useEffect(() => {
        getContributors()
    }, [])

    const getContributors = async () => {
        try {
            let response = await fetch(`/api/v1/relation/user_repository/${owner}/${repo}/contributors?login=${username}`)
            const json = await response.json()
            setContributors(json.information.information.contributors)
        } catch (e) {
            alert(e)
        }
    }

    const sections = {
        "Commits / Time": <CommitsByTime />
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <div style={{position:"relative", top: "15%", width: "87%"}}>
                <h2 style={{paddingBottom: 10}}><u>Colaboradores</u></h2>
                <ul style={{listStyleType: "none", maxHeight: "60vh", width:"50vh", overflowY: "auto"}}>
                    {contributors.map(contributor => {
                        return (
                            <li style={{display: "flex", flexDirection:"row", paddingBlock: 5}}>
                                <img style={{width: 60, height:60, borderRadius:"50%"}} src={contributor.avatarUrl} alt="imagen"/>
                                <p style={{margin: "20px"}}>{contributor.username}</p>
                            </li>
                        );
                    })}
                </ul>
            </div>
            <FolderTabs style={{position: "absolute", top: "20%", left: "40%", width: "80vh"}} sections={sections}/>
        </div>
    );
}

