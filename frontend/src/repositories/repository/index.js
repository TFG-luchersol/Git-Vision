import React, { useState, useEffect } from 'react';
import AccordionItem from '../AccordionItem.js';
import TreeFiles from './TreeFiles.js';

export default function Repository() {

    const [files, setFiles] = useState({})

    useEffect(() => {
        getFiles()
    }, [])

    const getFiles = async () => {
        try {
            let newRepositories = await fetch(`/api/v1/files/repository/1`)
            const json = await newRepositories.json()
            setFiles(json.data.data.tree)
        } catch (e) {
            alert(e)
        }
    }


    return (
        <div style={{marginLeft: '40px', marginTop: '20px'}}>
            <TreeFiles root={files} />
        </div>
    );
}