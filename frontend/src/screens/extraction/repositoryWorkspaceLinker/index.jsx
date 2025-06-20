import { useNotification } from '@context/NotificationContext';
import '@css';
import "@css/auth/authPage.css";
import '@css/extraction/repositoryWorkspaceLinker';
import '@css/home';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, Form, FormGroup, Input } from 'reactstrap';

export default function RepositoryWorkspaceLinker() {

    const {showMessage} = useNotification();

    const [values, setValues] = useState({ repository: "", workspace: "" });
    const [owner, setOwner] = useState("");
    const [repositories, setRepositories] = useState({});
    const [workspaces, setWorkspaces] = useState([]);
    const [dropdownOwnerOpen, setDropdownOwnerOpen] = useState(false);
    const [dropdownRepositoryOpen, setDropdownRepositoryOpen] = useState(false);
    const [dropdownWorkspaceOpen, setDropdownWorkspaceOpen] = useState(false);

    const toggleOwner = () => setDropdownOwnerOpen(prevState => !prevState);
    const toggleRepository = () => setDropdownRepositoryOpen(prevState => !prevState);
    const toggleWorkspace = () => setDropdownWorkspaceOpen(prevState => !prevState);

    useEffect(() => {
        const loadRepositories = async () => {
            try {
                const response = await fetchBackend(`/api/v1/relation/repository/not_linked`)
                const repositories = await getBody(response);
                setRepositories(repositories)
            } catch (error) {
                showMessage({
                    message: error.message
                });
            }
        };
        const loadWorkspaces = async () => {
            try {
                const response = await fetchBackend(`/api/v1/relation/workspace/not_linked`)
                const workspaces = await getBody(response);
                setWorkspaces(workspaces)
            } catch (error) {
                showMessage({
                    message: error.message
                })
            }
        };
        loadRepositories();
        loadWorkspaces();

    }, [])

    async function handleSubmit(event) {
        event.preventDefault()
        try {
            const url = `/api/v1/relation/repository/${owner}/${values.repository}/linker`;
            let queryParams = {"workspaceName": encodeURIComponent(values.workspace.name)}
            
            const response = await fetchBackend(url, {
                method: "POST",
            }, queryParams);
            
            if (response.status === 200) {
                window.location.href = "/";
            } else {
                await getBody(response);
            }
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }

    function handleChange(property, value) {
        let newValues = { ...values };
        newValues[property] = value;
        setValues(newValues)
    }

    function handleOwner(owner) {
        setOwner(owner)
        setValues({...values, repository: ""})
    }

    return (
        <div className='center-screen'>
            <Form onSubmit={handleSubmit} className='auth-form-container' >
                <div className='flex-container'>
                    <title className='center-title'>
                        <h1>Enlazar Github/Clockify</h1>
                    </title>
                    <div style={{ display:"flex", alignItems: "center", flexDirection: "column"}}>
                        <FormGroup style={{width: "80%"}}>
                            <Dropdown isOpen={dropdownOwnerOpen} toggle={toggleOwner} >
                                <DropdownToggle caret>
                                    Dueño
                                </DropdownToggle>
                                <div class="mb-1"/>
                                <DropdownMenu>
                                    {Object.keys(repositories).map(owner => <DropdownItem onClick={() => handleOwner(owner)}>{owner}</DropdownItem>)}
                                </DropdownMenu>
                            </Dropdown>
                            <Input value={owner || "Selecciona un dueño"} readOnly/>
                        </FormGroup>
                        <FormGroup style={{width: "80%"}}>
                            <Dropdown isOpen={dropdownRepositoryOpen} toggle={toggleRepository}>
                                <DropdownToggle caret>
                                    Repositorio
                                </DropdownToggle>
                                <div class="mb-1"/>
                                <DropdownMenu>
                                    {repositories[owner]?.map(repo => <DropdownItem id={repo} onClick={(event) => handleChange("repository", repo)}>{repo}</DropdownItem>)}
                                </DropdownMenu>
                            </Dropdown>
                            <Input value={values.repository || "Selecciona un repositorio"} readOnly/>
                        </FormGroup>
                        <FormGroup style={{width: "80%"}}>
                            <Dropdown isOpen={dropdownWorkspaceOpen} toggle={toggleWorkspace}>
                                <DropdownToggle caret>
                                    Workspace
                                </DropdownToggle>
                                <div class="mb-1"/>
                                <DropdownMenu>
                                    {workspaces.map(ws => <DropdownItem onClick={(event) => handleChange("workspace", ws)}>{ws.name}</DropdownItem>)}
                                </DropdownMenu>
                            </Dropdown>
                            <Input value={values.workspace.name || "Selecciona un workspace"} readOnly/>
                        </FormGroup>
                    </div>
                    <div className='button-group'>
                        <Button disabled={!(owner && values.repository && values.workspace)} type='submit'>Enlazar</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/repositories"}>Cancelar</Link>
                        </Button>
                    </div>
                </div>
            </Form>


        </div>
    );
};
