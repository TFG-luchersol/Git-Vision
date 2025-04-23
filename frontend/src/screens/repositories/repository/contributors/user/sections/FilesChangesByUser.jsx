import CommitsModal from '@components/CommitsModal'; // Asegúrate de tener este path correcto
import CounterChanges from '@components/CounterChanges';
import DateRangePicker from '@components/DateRangePicker';
import DelayedButton from '@components/DelayedButton';
import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/contributors/user/sections/filesChangesByUser";
import fetchBackend from '@utils/fetchBackend';
import getBody from '@utils/getBody';
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { Input, Label } from 'reactstrap';

export default function FilesChangesByUser() {
    const { showMessage } = useNotification();
    const { owner, repo, user } = useParams();
    
    const [isLoading, setIsLoading] = useState(false);
    const [files, setFiles] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [limit, setLimit] = useState(10);
    const [useAll, setUseAll] = useState(false); // Nueva bandera

    const [selectedFile, setSelectedFile] = useState(null);

    const getChangedFiles = async () => {
        try {
            setIsLoading(true);
            const queryParams = {
                author: user,
                startDate: startDate ? new Date(startDate).toISOString() : null,
                endDate: endDate ? new Date(endDate).toISOString() : null,
                limit: useAll ? null : limit
            };
            const url = `/api/v1/github_users/${owner}/${repo}/files`;
            const response = await fetchBackend(url, {}, queryParams);
            const filesWithChanges = await getBody(response);
            setFiles(filesWithChanges);
        } catch (error) {
            showMessage({ message: error.message });
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>
            <div className='items-column'>
                <DateRangePicker
                    startDate={startDate}
                    endDate={endDate}
                    setStartDate={setStartDate}
                    setEndDate={setEndDate}
                />
                <p style={{ marginLeft: "20px" }}>Num Commits: </p>
                <Input
                    style={{ marginInline: "20px", width: "20%" }}
                    type='number'
                    min={0}
                    value={limit ?? ''}
                    onChange={e => setLimit(Number(e.target.value))}
                    disabled={useAll}
                />
                <div style={{ marginLeft: "10px", display: "flex", alignItems: "center" }}>
                    <Input
                        type="checkbox"
                        checked={useAll}
                        onChange={() => setUseAll(!useAll)}
                    />
                    <Label style={{marginLeft: "5px"}} check>Usar todos</Label>
                </div>
                <DelayedButton
                    onClick={getChangedFiles}
                    disabled={isLoading}
                    className="button"
                    text={"Buscar"}
                />    
            </div>
            <div style={{ marginTop: 5 }}>
                <ul className='file-container'>
                    {files?.toSorted((a, b) => a.filePath.localeCompare(b.filePath))
                        .map((file, index) => (
                            <li
                                key={index}
                                className='file-row cursor-pointer hover:bg-gray-100'
                                onClick={() => setSelectedFile(file)}
                            >
                                {file.filePath}
                                <CounterChanges additions={file.change?.additions} deletions={file.change?.deletions}/>
                            </li>
                        ))}
                </ul>
            </div>

            {selectedFile && (
                <CommitsModal
                    isOpen={true}
                    onClose={() => setSelectedFile(null)}
                    changes={[selectedFile]}
                />
            )}
        </>
    );
}
