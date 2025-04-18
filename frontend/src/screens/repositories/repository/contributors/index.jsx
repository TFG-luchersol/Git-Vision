import Contributions from '@components/Contributions.jsx';
import FolderTabs from '@components/FolderTabs.jsx';
import React from 'react';
import { useParams } from 'react-router-dom';
import ContributorProfiles from './stadistics/ContributorProfiles';

export default function Contributors() {

    const {owner, repo} = useParams();

    const sections = {
        "Contributors": <ContributorProfiles />,
        "Commits / Time": <Contributions owner={owner} repo={repo} />,
    }

    return (
        <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
    );
}

