import React from 'react';
import { useParams } from 'react-router-dom';
import Contributions from '../../../components/Contributions.jsx';
import FolderTabs from '../../../components/FolderTabs.jsx';
import ContributorProfiles from './stadistics/ContributorProfiles';

export default function Contributors() {

    const {owner, repo} = useParams();

    const sections = {
        "Contributors": <ContributorProfiles />,
        "Commits / Time": <Contributions owner={owner} repo={repo} />,
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
        </div>
    );
}

