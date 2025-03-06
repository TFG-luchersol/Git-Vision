import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import tokenService from '../../../../services/token.service.js';
import fetchWithToken from '../../../../util/fetchWithToken.js';
import getBody from '../../../../util/getBody.js';

export default function ContributorProfiles() {

    const { username } = tokenService.getUser();
    const { owner, repo } = useParams();

    const [contributors, setContributors] = useState([]);

    useEffect(() => {
        getContributors()
    }, [])

    const getContributors = async () => {
        try {
            let response = await fetchWithToken(`/api/v1/relation/user_repository/${owner}/${repo}/contributors?login=${username}`)
            const json = await response.json()
            const result = getBody(json);
            setContributors(result.contributors)
        } catch (e) {
            alert(e)
        }
    }

    return (
        <ul style={{ listStyleType: "none", columnCount: 3 }}>
            {contributors.map(contributor => {
                return (
                    <li style={{ display: "flex", flexDirection: "row", paddingBlock: 5 }}>
                        <img style={{ width: 60, height: 60, borderRadius: "50%" }} src={contributor.avatarUrl} alt="imagen" />
                        <p style={{ margin: "20px" }}>{contributor.username}</p>
                    </li>
                );
            })}
        </ul>
    );
}