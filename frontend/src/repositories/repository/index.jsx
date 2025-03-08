import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, ButtonGroup, Input } from 'reactstrap';
import MultiSelectDropdown from '../../components/MultiSelectDropdown.jsx';
import PieChart from '../../components/PieChart.jsx';
import TreeFiles from '../../components/TreeFiles.jsx';
import '../../static/css/repositories/repository';
import fetchWithToken from '../../util/fetchWithToken.ts';
import getBody from '../../util/getBody.ts';


export default function Repository() {
    const { owner, repo } = useParams();

    const [files, setFiles] = useState({});
    const [filter, setFilter] = useState("");
    const [percentajeLanguajes, setPercentajeLanguajes] = useState({});
    const [showPercentaje, setShowPercentaje] = useState(false)
    const [numFiles, setNumFiles] = useState(0);
    const [numBytes, setNumBytes] = useState(0);
    const [extensionCounter, setExtensionCounter] = useState({})
    const [selectedOptions, setSelectedOptions] = useState([]);

    useEffect(() => {
        getFiles()
        getPercentajeLanguajes()
        getExtensionCounter()
    }, [])

    const getFiles = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newFiles = await fetchWithToken(`/api/v1/files/repository/${id}`)
            const {tree} = await getBody(newFiles);
            setFiles(tree)
        } catch (e) {
            alert(e.message)
        }
    }

    const getExtensionCounter = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newFiles = await fetchWithToken(`/api/v1/files/repository/${id}/extension_counter`)
            const {extensionCounter} = await getBody(newFiles)
            const numFiles = Object.values(extensionCounter).reduce((acc, next) => acc + next, 0);
            setNumFiles(numFiles);
            setExtensionCounter(extensionCounter);
        } catch (e) {
            alert(e.message)
        }
    }
    
    const getPercentajeLanguajes = async () => {
        try {
            const id = `${owner}/${repo}`;
            let newPercentageLanguages = await fetchWithToken(`/api/v1/files/languajes/repository/${id}`)
            const {percentageLanguages} = await getBody(newPercentageLanguages)
            setPercentajeLanguajes(percentageLanguages.percentages)
            setNumBytes(percentageLanguages.numBytes);
        } catch (e) {
            alert(e.message)
        }
    }

    return (
        <div style={{ position: "fixed", top: 0, zIndex: -1, left: 0, right: 0, bottom: 0, backgroundColor: "#dcdcdc" }}>
            <div style={{ marginLeft: '40px', marginTop: "8%", display: 'flex', flexDirection: 'row', maxHeight: "80%" }}>
                <div className='contenedor-archivos'>
                    <ButtonGroup >
                        <Button style={{backgroundColor: "#007bff", height:35}} onClick={() => window.location.href += "/details"}>DETAILS</Button>
                        <Button style={{backgroundColor: "#28a745",height:35}} onClick={() => window.location.href += "/commits"}>COMMITS</Button>
                        <Button style={{backgroundColor: "#fd7e14",height:35}} onClick={() => window.location.href += "/issues"}>ISSUES</Button>
                        <Button style={{backgroundColor: "#8ef9c0",height:35}} onClick={() => window.location.href += "/contributors"}>CONTRIBUTORS</Button>
                    </ButtonGroup> 
                    <div style={{ display: "flex", flexDirection: "row" }}>
                        <Input type='text' value={filter} onChange={(e) => setFilter(e.target.value)}/>
                        &nbsp;&nbsp;
                        <MultiSelectDropdown
                            selectedOptions={selectedOptions}
                            setSelectedOptions={setSelectedOptions}
                            width={400}
                            options={[...Object.keys(extensionCounter).filter(value => value !== "Unknown")].sort().concat("Unknown")}
                        />
                    </div>
                    <TreeFiles deepFilter root={files} filter={filter} filterExtension={selectedOptions} className={"archivo"} />
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
                                {
                                Object.keys(extensionCounter).includes("Unknown") ?
                                [...Object.keys(extensionCounter).filter(value => value !== "Unknown")].sort().concat("Unknown").map((key, index) =>
                                    <tr key={index}>
                                        <td>{key}</td>
                                        <td>{extensionCounter[key]}</td>
                                        <td>{((extensionCounter[key] / numFiles) * 100).toFixed(2)}%</td>
                                    </tr>
                                ) :
                                [...Object.keys(extensionCounter)].sort().map((key, index) =>
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

