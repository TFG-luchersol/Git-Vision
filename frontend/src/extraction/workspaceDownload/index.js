import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Alert, Button, Form, FormGroup } from 'reactstrap';
import '../../App.css';
import CustomInput from '../../components/CustomInput.js';
import LoadingModal from '../../components/LoadingModal.js';
import tokenService from '../../services/token.service.js';
import "../../static/css/auth/authPage.css";
import '../../static/css/home/home.css';
import Preconditions from '../../util/check.js';
import fetchWithToken from '../../util/fetchWithToken.js';

export default function WorkspaceDownload(){

    const [message, setMessage] = useState(null)
    const [values, setValues] = useState({ name: "", id: "" })
    const [isLoading, setIsLoading] = useState(false)

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            setMessage(null);
            Preconditions.checkNotBlank(values.id, "Id")
            Preconditions.checkNotBlank(values.name, "Name")
            setIsLoading(true);
            const response = await fetchWithToken(`/api/v1/relation/user_workspace?workspaceId=${values.id}&name=${values.name}`, {
                method: "POST",
                body: tokenService.getUser().username
            });
            if (response.status === 200) {
                window.location.href = "/";
            } else {
                const error = await response.json();
                setMessage(error.message)
            }
        } catch (error) {
            alert(error)
            // setMessage(error.message);
        } finally {
            setIsLoading(false);
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
            <LoadingModal isLoading={isLoading} />
            <Alert isOpen={message} color="danger" style={{position:'absolute', top:'30px'}}>{message}</Alert>
                
                <Form onSubmit={handleSubmit} className='auth-form-container' >
                    <div style={{margin: "30px"}}>
                        <title className='center-title'>
                            <h1>Download Workspace</h1>
                        </title>

                        <FormGroup>
                            <CustomInput
                                label="Id:"
                                type='text' 
                                name='id' 
                                value={values.id}
                                onChange={handleChange}
                            />
                        </FormGroup>
                        <FormGroup>
                            <CustomInput
                                label="Name:"
                                type='text'
                                name='name'
                                value={values.name}
                                onChange={handleChange}
                            />
                        </FormGroup>

                        <div className='button-group'>
                            <Button type='submit'>Download</Button> 
                            <Button type='button'>
                                <Link className='custom-link' to={"/repositories"}>Cancel</Link>
                            </Button> 
                        </div>
                    </div>
                </Form>


        </div>
    );
};