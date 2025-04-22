import MultiSelectDropdown from '@components/MultiSelectDropdown.jsx';
import PieChart from '@components/PieChart.jsx';
import TreeFiles from '@components/TreeFiles.jsx';
import { useNotification } from '@context/NotificationContext';
import '@css/repositories/repository';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, ButtonGroup, Input } from 'reactstrap';


export default function Repository() {
    const { showMessage } = useNotification();
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
        getFiles();
        getPercentajeLanguajes();
        getExtensionCounter();
    }, [])

    const getFiles = async () => {
        try {
            let response = await fetchBackend(`/api/v1/files/repository/${owner}/${repo}`)
            const tree = await getBody(response);
            setFiles(tree)
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    const getExtensionCounter = async () => {
        try {
            let response = await fetchBackend(`/api/v1/files/repository/${owner}/${repo}/extension_counter`)
            const extensionCounter = await getBody(response)
            const numFiles = Object.values(extensionCounter).reduce((acc, next) => acc + next, 0);
            setNumFiles(numFiles);
            setExtensionCounter(extensionCounter);
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }
    
    const getPercentajeLanguajes = async () => {
        try {
            let response = await fetchBackend(`/api/v1/files/languajes/repository/${owner}/${repo}`)
            const percentageLanguages = await getBody(response)
            setPercentajeLanguajes(percentageLanguages.percentages)
            setNumBytes(percentageLanguages.numBytes);
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    return (
        <div style={{ marginLeft: '40px', marginTop: "1%", display: 'flex', flexDirection: 'row', maxHeight: "80%" }}>
            <div className='contenedor-archivos'>
                <ButtonGroup >
                    <Button style={{backgroundColor: "#007bff", height:35}} onClick={() => window.location.href += "/details"}>DETAILS</Button>
                    <Button style={{backgroundColor: "#28a745",height:35}} onClick={() => window.location.href += "/commits"}>COMMITS</Button>
                    <Button style={{backgroundColor: "#fd7e14",height:35}} onClick={() => window.location.href += "/issues"}>ISSUES</Button>
                    <Button style={{backgroundColor: "#8ef9c0",height:35}} onClick={() => window.location.href += "/contributors"}>CONTRIBUTORS</Button>
                    <Button style={{backgroundColor: "#777777",height:35}} onClick={() => window.location.href += "/configuration"}>CONFIGURATION</Button>
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
    );
}

