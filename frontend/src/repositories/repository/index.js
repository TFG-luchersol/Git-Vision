import React, { useState, useEffect } from 'react';
import TreeFiles from '../../components/TreeFiles.js'
import PieChart from '../../components/PieChart.js';
import { useParams } from 'react-router-dom';
import tokenService from '../../services/token.service.js';
import './repository.css';
import { Input } from 'reactstrap';

export default function Repository() {
    const {username} = tokenService.getUser();
    const {owner, repo} = useParams();

    const [files, setFiles] = useState({});
    const [percentajeLanguajes, setPercentajeLanguajes] = useState({});
    const [showPercentaje, setShowPercentaje] = useState(false)
    const [numFiles, setNumFiles] = useState(0);

    useEffect(() => {
        getFiles()
        getPercentajeLanguajes()
    }, [])

    const getFiles = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newFiles = await fetch(`/api/v1/files/repository/${id}?login=${username}`)
            const json = await newFiles.json()
            setFiles(json.information.information.tree)
        } catch (e) {

        }
    }

    const getPercentajeLanguajes = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newPercentajeLanguajes = await fetch(`/api/v1/files/languajes/repository/${id}?login=${username}`)
            const json = await newPercentajeLanguajes.json()
            setPercentajeLanguajes(json.information.information.percentageLanguages.percentages)
            setNumFiles(json.information.information.percentageLanguages.numFiles);
        } catch (e) {

        }
    }


    return (
        <div style={{position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc"}}>
            <div style={{marginLeft: '40px', marginTop:"8%", display:'flex', flexDirection:'row', maxHeight: "80%"}}>
                <div className='contenedor-archivos'>
                    <TreeFiles root={files} className={"archivo"} />
                </div>
                <div style={{position:'relative', left:'50px', display: "flex", flexDirection: "column"}}>
                    <PieChart numFiles={numFiles} showPercentaje={showPercentaje} labels={Object.keys(percentajeLanguajes)} data={Object.values(percentajeLanguajes)}/>
                    <div>
                        MOSTRAR PORCENTAJE:
                        <Input style={{position: "relative", left: 5}} checked={showPercentaje} type='checkbox' onClick={() => setShowPercentaje(prev => !prev)} />
                    </div>
                </div>
            </div>
        </div>
    );
}