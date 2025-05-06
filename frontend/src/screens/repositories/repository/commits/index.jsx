import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/commits";
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { FaSearch, FaSpinner, FaTimes } from 'react-icons/fa'; // Íconos para los botones
import { VscRegex } from "react-icons/vsc";
import { useParams } from 'react-router-dom';
import { Button, Input } from 'reactstrap';

export default function Commits() {
    const { showMessage } = useNotification();
    const { owner, repo } = useParams();

    const [commits, setCommits] = useState([]);
    const [filteredCommits, setFilteredCommits] = useState([]);
    const [filterText, setFilterText] = useState('');
    const [page, setPage] = useState(1);
    const [isRegex, setIsRegex] = useState(false);
    const [isFiltered, setIsFiltered] = useState(false);
    const [loading, setLoading] = useState(false);  // Nuevo estado para la carga de datos

    useEffect(() => {
        getCommits();
    }, [page]);

    const getCommits = async () => {
        try {
            setLoading(true);  // Inicia la carga
            const repositoryName = `${owner}/${repo}`;
            const response = await fetchBackend(`/api/v1/commits/${repositoryName}?page=${page}`);
            const { commits } = await getBody(response);
            setCommits(commits);
            setFilteredCommits(commits);
        } catch (error) {
            showMessage({
                message: error.message,
            });
        } finally {
            setLoading(false);  // Finaliza la carga
        }
    };

    const filterCommits = async () => {
        try {
            setLoading(true);  // Inicia la carga
            // Construimos la URL con los parámetros de búsqueda y regex
            const filterParam = filterText ? encodeURIComponent(filterText) : '';
            const regexParam = isRegex ? 'true' : 'false';
    
            // Si no hay texto en el filtro, simplemente volvemos a obtener todos los commits
            if (filterText === '') {
                // Limpiar el filtro y obtener todos los commits
                await getCommits();
                setIsFiltered(false);
                return;
            }
            console.log(1)
            console.log(`/api/v1/commits/${owner}/${repo}?filter=${filterParam}&isRegex=${regexParam}`)
            const response = await fetchBackend(`/api/v1/commits/${owner}/${repo}?filter=${filterParam}&isRegex=${regexParam}`);
            console.log(2)
            // Obtener la respuesta y actualizar el estado
            const { commits } = await getBody(response);
    
            setFilteredCommits(commits);
            setIsFiltered(true);
        } catch (error) {
            showMessage({
                message: error.message,
            });
        } finally {
            setLoading(false);  // Finaliza la carga
        }
    };

    const handleFilterTextChange = (e) => {
        setFilterText(e.target.value);
    };

    const nextPage = () => setPage(page + 1);
    const previousPage = () => setPage(page <= 1 ? 1 : page - 1);

    const clearFilter = () => {
        setFilterText('');
        setIsFiltered(false);
        setIsRegex(false);
        setPage(1);
        getCommits();
    };

    const toggleRegexFilter = () => {
        setIsRegex(!isRegex);
    };

    const applyFilter = () => {
        filterCommits();
    };

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

            <ul className="commits-container">
                {(isFiltered ? filteredCommits : commits).map((commit, index) => (
                    <li
                        className="commit-row"
                        key={index}
                        onClick={() => (window.location.href += '/' + commit.sha)}
                    >
                        <div>
                            <span className="title">{commit.message}</span>
                        </div>
                    </li>
                ))}
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
