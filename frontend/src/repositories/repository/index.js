import React, { useState, useEffect } from 'react';
import AccordionItem from '../AccordionItem.js';
import TreeFiles from './TreeFiles.js';
import PieChart from './PieChart.js';

export default function Repository() {

    const [files, setFiles] = useState({})
    const [percentajeLanguajes, setPercentajeLanguajes] = useState({})

    useEffect(() => {
        getFiles()
        getPercentajeLanguajes()
    }, [])

    const getFiles = async () => {
        try {
            let newFiles = await fetch(`/api/v1/files/repository/1`)
            const json = await newFiles.json()
            setFiles(json.data.information.tree)
        } catch (e) {
            alert(e)
        }
    }

    const getPercentajeLanguajes = async () => {
        try {
            let newPercentajeLanguajes = await fetch(`/api/v1/files/languajes/repository/1`)
            const json = await newPercentajeLanguajes.json()
            setPercentajeLanguajes(json.data.information.percentageLanguages.percentages)
        } catch (e) {
            alert(e)
        }
    }


    return (
        <div style={{marginLeft: '40px', marginTop: '20px', display:'flex', flexDirection:'row'}}>
            <div style={{width:'500px', overflowX:'auto'}}>
                <TreeFiles root={files} />
            </div>
            <div style={{position:'relative', left:'300px'}}>
                <PieChart labels={Object.keys(percentajeLanguajes)} data={Object.values(percentajeLanguajes)}/>
            </div>
        </div>
    );
}