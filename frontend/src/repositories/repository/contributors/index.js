import React from 'react';
import FolderTabs from '../../../components/FolderTabs.js';
import CommitsByTime from './stadistics/CommitsByTime.js';
import ContributorProfiles from './stadistics/ContributorProfiles.js';

export default function Contributors() {

    const sections = {
        "Contributors": <ContributorProfiles />,
        "Commits / Time": <CommitsByTime />
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
        </div>
    );
}

