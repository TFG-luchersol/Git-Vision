import React, { useEffect, useState } from 'react';
import '../../App.css';
import '../../static/css/home/home.css';
import './repositoryWorkspaceLinker.css'
import { Alert, Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, Form, FormGroup, Input, Label } from 'reactstrap';
import tokenService from "../../services/token.service.js";
import "../../static/css/auth/authPage.css";
import { Link } from 'react-router-dom';
import CustomInput from '../../components/CustomInput.js';

export default function RepositoryWorkspaceLinker() {

    const [message, setMessage] = useState(null);
    const [values, setValues] = useState({ repository: "", workspace: "" });
    const [repositories, setRepositories] = useState({});
    const [owner, setOwner] = useState("");
    const [workspaces, setWorkspaces] = useState([]);
    const [dropdownOwnerOpen, setDropdownOwnerOpen] = useState(false);
    const [dropdownRepositoryOpen, setDropdownRepositoryOpen] = useState(false);
    const [dropdownWorkspaceOpen, setDropdownWorkspaceOpen] = useState(false);

    const toggleOwner = () => setDropdownOwnerOpen(prevState => !prevState);
    const toggleRepository = () => setDropdownRepositoryOpen(prevState => !prevState);
    const toggleWorkspace = () => setDropdownWorkspaceOpen(prevState => !prevState);

    useEffect(() => {
        const userId = tokenService.getUser().id
        const loadRepositories = async () => {
            const response = await fetch(`/api/v1/relation/user_repository/repositories?userId=${userId}`)
            if (response.status === 200) {
                const json = await response.json()
                setRepositories(json.information.information.repositories)
            } else {
                const error = await response.json();
                setMessage(error.message)
            }
        };
        const loadWorkspaces = async () => {
            const response = await fetch(`/api/v1/relation/user_workspace/workspaces?userId=${userId}`)
            if (response.status === 200) {
                const json = await response.json()
                setWorkspaces(json.information.information.workspaces)
            } else {
                const error = await response.json();
                setMessage(error.message)
            }
        };
        loadRepositories();
        loadWorkspaces();

    }, [])

    async function handleSubmit(event) {
        event.preventDefault()
        setMessage(null);
        try {
            const repositoryName = `${owner}/${values.repository}`
            const response = await fetch(`/api/v1/linker?repositoryName=${repositoryName}&workspaceName=${values.workspace.name}&userId=${tokenService.getUser().id}`, {
                method: "POST",
            });

            if (response.status === 200) {
                window.location.href = "/";
            } else {
                const error = await response.json();
                setMessage(error.message)
            }
        } catch (error) {
            setMessage(error.message);
        }
    }

    function handleChange(property, value) {
        let newValues = { ...values };
        newValues[property] = value;
        setValues(newValues)
    }

    return (
        <div className="home-page-container">
            <Alert isOpen={message} color="danger" style={{ position: 'fixed', top: '11%'}}>{message}</Alert>

            <Form onSubmit={handleSubmit} className='auth-form-container' >
                <div className='flex-container'>
                    <title className='center-title'>
                        <h1>Link Github/Clockify</h1>
                    </title>
                    <div style={{ display:"flex", alignItems: "center", flexDirection: "column"}}>
                        <FormGroup>
                            <Dropdown isOpen={dropdownOwnerOpen} toggle={toggleOwner} >
                                <DropdownToggle caret>
                                    Owner
                                </DropdownToggle>
                                <div class="mb-1"/>
                                <DropdownMenu>
                                    {Object.keys(repositories).map(owner => <DropdownItem onClick={() => setOwner(owner)}>{owner}</DropdownItem>)}
                                </DropdownMenu>
                            </Dropdown>
                            <Input value={owner || "Select a Owner"} readOnly/>
                        </FormGroup>
                        <FormGroup>
                            <Dropdown isOpen={dropdownRepositoryOpen} toggle={toggleRepository}>
                                <DropdownToggle caret>
                                    Repository
                                </DropdownToggle>
                                <div class="mb-1"/>
                                <DropdownMenu>
                                    {repositories[owner]?.map(repo => <DropdownItem id={repo} onClick={(event) => handleChange("repository", repo)}>{repo}</DropdownItem>)}
                                </DropdownMenu>
                            </Dropdown>
                            <Input value={values.repository || "Select a Repository"} readOnly/>
                        </FormGroup>
                        <FormGroup>
                            <Dropdown isOpen={dropdownWorkspaceOpen} toggle={toggleWorkspace}>
                                <DropdownToggle caret>
                                    Workspace
                                </DropdownToggle>
                                <div class="mb-1"/>
                                <DropdownMenu>
                                    {workspaces.map(ws => <DropdownItem onClick={(event) => handleChange("workspace", ws)}>{ws.name}</DropdownItem>)}
                                </DropdownMenu>
                            </Dropdown>
                            <Input value={values.workspace.name || "Select a Workspace"} readOnly/>
                        </FormGroup>
                    </div>
                    <div className='button-group'>
                        <Button type='submit'>Link</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/repositories"}>Cancel</Link>
                        </Button>
                    </div>
                </div>
            </Form>


        </div>
    );
};