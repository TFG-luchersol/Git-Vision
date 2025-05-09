import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/issues";
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { FaHashtag, FaSearch, FaSpinner, FaTimes } from 'react-icons/fa';
import { GoIssueClosed, GoIssueOpened } from 'react-icons/go';
import { VscRegex } from "react-icons/vsc";
import { useParams } from 'react-router-dom';
import { Button, Input } from 'reactstrap';

export default function Issues() {
    const { showMessage } = useNotification();
    const { owner, repo } = useParams();

    const [issues, setIssues] = useState([]);
    const [filteredIssues, setFilteredIssues] = useState([]);
    const [filterText, setFilterText] = useState('');
    const [page, setPage] = useState(1);
    const [isRegex, setIsRegex] = useState(false);
    const [isFiltered, setIsFiltered] = useState(false);
    const [isIssueNumber, setIsIssueNumber] = useState(false); // Nuevo estado
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        getIssues();
    }, [page]);

    const getIssues = async () => {
        try {
            setLoading(true);
            const repositoryName = `${owner}/${repo}`;
            const response = await fetchBackend(`/api/v1/issues/${repositoryName}?page=${page}`);
            const issues = await getBody(response);
            
            if (issues.length === 0 && page > 1) {
                // Si no hay resultados y la página es mayor que 1, retrocedemos a la página anterior
                setPage(page - 1);
                return;
            }

            setIssues(issues);
            setFilteredIssues(issues);
        } catch (error) {
            showMessage({ message: error.message });
        } finally {
            setLoading(false);
        }
    };

    const filterIssues = async () => {
        try {
            setLoading(true);

            const filterParam = filterText ? encodeURIComponent(filterText) : '';
            const regexParam = isRegex ? 'true' : 'false';
            const issueNumParam = isIssueNumber ? 'true' : 'false';

            if (filterText === '') {
                await getIssues();
                setIsFiltered(false);
                return;
            }

            const url = `/api/v1/issues/${owner}/${repo}?filter=${filterParam}&isRegex=${regexParam}&isIssueNumber=${issueNumParam}`;
            const response = await fetchBackend(url);
            const issues = await getBody(response);

            if (issues.length === 0 && page > 1) {
                setPage(page - 1); // Retroceder página si no hay resultados
                return;
            }

            setFilteredIssues(issues);
            setIsFiltered(true);
        } catch (error) {
            showMessage({ message: error.message });
        } finally {
            setLoading(false);
        }
    };

    const handleFilterTextChange = (e) => {
        setFilterText(e.target.value);
    };

    const clearFilter = () => {
        setFilterText('');
        setIsFiltered(false);
        setIsRegex(false);
        setIsIssueNumber(false);
        setPage(1);
        getIssues();
    };

    const toggleRegexFilter = () => {
        if (isIssueNumber) {
            setIsIssueNumber(false);
        }
        setIsRegex(!isRegex);
    };
    
    const toggleIssueNumberFilter = () => {
        if (isRegex) {
            setIsRegex(false);
        }
        setIsIssueNumber(!isIssueNumber);
    };
    const applyFilter = () => filterIssues();
    const nextPage = () => setPage(page + 1);
    const previousPage = () => setPage(Math.max(1, page - 1));

    return (
        <div style={{ display: 'flex', flexDirection: 'column', position: 'absolute', top: '15%', width: '87%' }}>
            <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                <Input
                    value={filterText}
                    onChange={handleFilterTextChange}
                    style={{ marginLeft: '7%', marginRight: '10px', flex: 1 }}
                />
                <Button
                    onClick={toggleRegexFilter}
                    style={{
                        marginRight: '10px',
                        padding: '10px',
                        backgroundColor: isRegex ? 'orange' : 'lightgray',
                        color: 'white',
                        border: 'none',
                        width: '50px',
                        height: '50px',
                    }}
                    title="Activar/Desactivar Regex"
                >
                    <VscRegex />
                </Button>
                <Button
                    onClick={toggleIssueNumberFilter}
                    style={{
                        marginRight: '10px',
                        padding: '10px',
                        backgroundColor: isIssueNumber ? 'green' : 'lightgray',
                        color: 'white',
                        border: 'none',
                        width: '50px',
                        height: '50px',
                    }}
                    title="Buscar por Número de Issue"
                >
                    <FaHashtag />
                </Button>
                <Button
                    onClick={applyFilter}
                    style={{
                        marginRight: '10px',
                        padding: '10px',
                        backgroundColor: 'blue',
                        color: 'white',
                        border: 'none',
                        width: '50px',
                        height: '50px',
                    }}
                    title="Buscar"
                    disabled={!filterText || loading}
                >
                    {loading ? <FaSpinner className="spinner" /> : <FaSearch />}
                </Button>
                <Button
                    onClick={clearFilter}
                    style={{
                        padding: '10px',
                        backgroundColor: 'red',
                        color: 'white',
                        border: 'none',
                        width: '50px',
                        height: '50px',
                    }}
                    title="Cancelar Filtro"
                >
                    <FaTimes />
                </Button>
            </div>
            <ul className='issues-container'>
                {(isFiltered ? filteredIssues : issues).map((issue, index) =>
                    <li className="issue-row" key={index}
                        onClick={() => window.location.href += '/' + issue.number}>
                        <div style={{display:"flex", alignItems:"column"}}>
                            <div style={{marginRight:"10px"}}>
                                {issue.state.toUpperCase() === "CLOSED" ?
                                    <GoIssueClosed color='purple' /> :
                                    <GoIssueOpened color='green' />
                                }
                            </div>
                            <span className="title">{issue.title}</span>
                        </div>
                        <div>
                            <span className="number">#{issue.number}</span>
                        </div>
                    </li>
                )}
                <div style={{ position: "relative", top: 20, display: "flex", justifyContent: "center" }}></div>
            </ul>

            {!isFiltered && (
                <div style={{ position: 'relative', top: 20, display: 'flex', justifyContent: 'center' }}>
                    <Button onClick={previousPage} disabled={loading}>
                        Anterior
                    </Button>
                    <div style={{ padding: 10, paddingInline: 20 }}>{page}</div>
                    <Button onClick={nextPage} disabled={loading}>
                        Siguiente
                    </Button>
                </div>
            )}
        </div>
    );
}
