import Contributions from '@components/Contributions.jsx';
import FolderTabs from '@components/FolderTabs.jsx';
import React from 'react';
import { useParams } from 'react-router-dom';
import BasicStatistics from './stadistics/BasicStatistics';
import ContributorProfiles from './stadistics/ContributorProfiles';
import LinePerTimeInIssue from './stadistics/LinePerTimeInIssue';

export default function Contributors() {

    const {owner, repo} = useParams();

    const sections = {
        "Contribuidores": <ContributorProfiles />,
        "Estadísticas básicas": <BasicStatistics />,
        "Commits / Tiempo": <Contributions owner={owner} repo={repo} />,
        "Issue": <LinePerTimeInIssue />,
    }


    return (
        <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
    );
}

