import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import FolderTabs from '../../../components/FolderTabs';
import FileContent from './sections/FileContent';

export default function File() {

    const sections = {
        "Content": <FileContent />,
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <FolderTabs style={{position: "absolute", top: "15%", width: "200vh", height: "80vh"}} sections={sections}/>
        </div>
    );
}