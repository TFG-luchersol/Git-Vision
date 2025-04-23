import Contributions from '@components/Contributions.jsx';
import FolderTabs from '@components/FolderTabs.jsx';
import tokenService from '@services/token.service';
import React from 'react';
import { useParams } from 'react-router-dom';
import ContributorProfiles from './stadistics/ContributorProfiles';
import LinePerTimeInIssue from './stadistics/LinePerTimeInIssue';

export default function Contributors() {

    const {owner, repo} = useParams();

    const clockify_sections = {
        "Line / Time": <LinePerTimeInIssue />
    }
    // tokenService.hasClockifyToken();
    const sections = {
        "Contributors": <ContributorProfiles />,
        "Commits / Time": <Contributions owner={owner} repo={repo} />,
        ...(tokenService.hasClockifyToken() ? clockify_sections : {})
    }


    return (
        <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
    );
}

