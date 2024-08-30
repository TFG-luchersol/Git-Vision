import React, { useEffect, useState } from 'react';
import '../../App.css';
import '../../static/css/home/home.css';
import { Alert, Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, Form, FormGroup, Input, Label } from 'reactstrap';
import tokenService from "../../services/token.service.js";
import "../../static/css/auth/authPage.css";
import { Link } from 'react-router-dom';
import CustomInput from '../../components/CustomInput.js';

export default function RepositoryWorkspaceLinker() {

    const [message, setMessage] = useState(null);
    const [values, setValues] = useState({ repository: "", workspace: "" });
    const [repositories, setRepositories] = useState([]);
    const [dropdownOwnerOpen, setDropdownOwnerOpen] = useState(false);
    const [dropdownRepositoryOpen, setDropdownRepositoryOpen] = useState(false);

    const toggleOwner = () => setDropdownOwnerOpen(prevState => !prevState);
    const toggleRepository = () => setDropdownRepositoryOpen(prevState => !prevState);

    useEffect(() => {
        const loadRepositories = async () => {
            await fetch()
        };
        const loadWorkspace = async () => {
            await fetch()
        };
    }, [])

    async function handleSubmit(event) {
        event.preventDefault()
        setMessage(null);
        try {
            const response = await fetch("/api/v1/linker", {
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

    function handleChange(event) {
        const target = event.target;
        const { name, value } = target;
        let newValues = { ...values };
        newValues[name] = value;
        setValues(newValues)
    }

    return (
        <div className="home-page-container">

            <Alert isOpen={message} color="danger" style={{ position: 'absolute', top: '30px' }}>{message}</Alert>

            <Form onSubmit={handleSubmit} className='auth-form-container' >
                <div style={{ margin: "30px" }}>
                    <title className='center-title'>
                        <h1>Link Github/Clockify</h1>
                    </title>
                    <FormGroup style={{ display: 'flex', flexDirection: 'row' }}>
                        <Dropdown isOpen={dropdownOwnerOpen} toggle={toggleOwner} style={{marginRight: 10}}>
                            <DropdownToggle caret>
                                Owner
                            </DropdownToggle>
                            <DropdownMenu>
                                <DropdownItem>Action 1</DropdownItem>
                                <DropdownItem>Action 2</DropdownItem>
                                <DropdownItem>Action 3</DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                        <Input value="asdf"></Input>
                        <Dropdown isOpen={dropdownRepositoryOpen} toggle={toggleRepository}>
                            <DropdownToggle caret>
                                Repository
                            </DropdownToggle>
                            <DropdownMenu>
                                <DropdownItem>Action 1</DropdownItem>
                                <DropdownItem>Action 2</DropdownItem>
                                <DropdownItem>Action 3</DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                        <Input value="asdf"></Input>
                    </FormGroup>
                    <FormGroup>
                        <CustomInput
                            label={"Workspace:"}
                            type='text'
                            name='workspace'
                            value={values.workspace}
                            onChange={handleChange}
                        />
                    </FormGroup>

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