import { useNotification } from '@context/NotificationContext';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

export default function ContributorProfiles() {
    const { showMessage } = useNotification();
    const { owner, repo } = useParams();
    const navigate = useNavigate();

    const [contributors, setContributors] = useState([]);

    useEffect(() => {
        getContributors()
    }, [])

    const getContributors = async () => {
        try {
            let response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/contributors`)
            const contributors = await getBody(response);
            setContributors(contributors)
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    return (
        <ul style={{ listStyleType: "none", columnCount: 3 }}>
            {contributors.map(contributor => {
                return (
                    <li 
                        onClick={() => navigate(`/repository/${owner}/${repo}/contributors/${contributor.username}`)}
                        style={{ display: "flex", flexDirection: "row", paddingBlock: 5, cursor: "pointer" }}>
                        <img style={{ width: 60, height: 60, borderRadius: "50%" }} src={contributor.avatarUrl} alt="imagen" />
                        <p style={{ margin: "20px" }}>{contributor.username}</p>
                    </li>
                );
            })}
        </ul>
    );
}
