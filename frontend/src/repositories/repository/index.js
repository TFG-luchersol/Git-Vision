import React, { useState, useEffect } from 'react';
import TreeFiles from '../../components/TreeFiles.js'
import PieChart from '../../components/PieChart.js';
import { useParams } from 'react-router-dom';
import tokenService from '../../services/token.service.js';
import './repository.css';
import { Input } from 'reactstrap';

export default function Repository() {
    const { username } = tokenService.getUser();
    const { owner, repo } = useParams();

    const [files, setFiles] = useState({});
    const [percentajeLanguajes, setPercentajeLanguajes] = useState({});
    const [showPercentaje, setShowPercentaje] = useState(false)
    const [numFiles, setNumFiles] = useState(0);
    const [numBytes, setNumBytes] = useState(0);
    const [extensionCounter, setExtensionCounter] = useState({})

    useEffect(() => {
        getFiles()
        getPercentajeLanguajes()
        getExtensionCounter()
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

    const getExtensionCounter = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newFiles = await fetch(`/api/v1/files/repository/${id}/extension_counter?login=${username}`)
            const json = await newFiles.json()
            setExtensionCounter(json.information.information.extensionCounter)
            setNumFiles(Object.values(extensionCounter).reduce((acc,next)=> acc+next), 0);
        } catch (e) {

        }
    }

    const getPercentajeLanguajes = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newPercentajeLanguajes = await fetch(`/api/v1/files/languajes/repository/${id}?login=${username}`)
            const json = await newPercentajeLanguajes.json()
            setPercentajeLanguajes(json.information.information.percentageLanguages.percentages)
            setNumBytes(json.information.information.percentageLanguages.numBytes);
        } catch (e) {

        }
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <div style={{ marginLeft: '40px', marginTop: "8%", display: 'flex', flexDirection: 'row', maxHeight: "80%" }}>
                <div className='contenedor-archivos'>
                    <TreeFiles root={files} className={"archivo"} />
                </div>
                <div style={{ position: 'relative', left: '50px', display: "flex", flexDirection: "row" }}>
                    <div style={{ display: "flex", flexDirection: "column" }}>
                        <PieChart numBytes={numBytes} showPercentaje={showPercentaje} labels={Object.keys(percentajeLanguajes)} data={Object.values(percentajeLanguajes)} />
                        <div>
                            MOSTRAR PORCENTAJE:
                            <Input style={{ position: "relative", left: 5 }} checked={showPercentaje} type='checkbox' onClick={() => setShowPercentaje(prev => !prev)} />
                        </div>
                    </div>
                    <div className='extension-table' style={{ position: "relative", left: 20, top: 40 }}>
                        <table className='extension-table' >
                            <thead>
                                <tr>
                                    <th>Extension</th>
                                    <th>Contador</th>
                                    <th>Porcentaje</th>
                                </tr>
                            </thead>
                            <tbody>
                                {Object.keys(extensionCounter).map((key, index) =>
                                    <tr key={index}>
                                        <td>{key}</td>
                                        <td>{extensionCounter[key]}</td>
                                        <td>{((extensionCounter[key] / numFiles) * 100).toFixed(2)}%</td>
                                    </tr>
                                )
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}