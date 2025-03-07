import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import fetchWithToken from '../../../../util/fetchWithToken.ts';
import getBody from '../../../../util/getBody.ts';

export default function ContributorProfiles() {

    const { owner, repo } = useParams();

    const [contributors, setContributors] = useState([]);

    useEffect(() => {
        getContributors()
    }, [])

    const getContributors = async () => {
        try {
            let response = await fetchWithToken(`/api/v1/relation/user_repository/${owner}/${repo}/contributors`)
            const result = await getBody(response);
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