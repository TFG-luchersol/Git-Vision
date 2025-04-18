import CustomInput from '@components/CustomInput.jsx';
import LoadingModal from '@components/LoadingModal.jsx';
import { useNotification } from '@context/NotificationContext';
import '@css';
import "@css/auth/authPage.css";
import '@css/home';
import tokenService from '@services/token.service.js';
import Preconditions from '@utils/check.js';
import fetchWithToken from '@utils/fetchWithToken.ts';
import getBody from '@utils/getBody';
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Form, FormGroup } from 'reactstrap';

export default function WorkspaceDownload(){
const { showMessage } = useNotification();
    const [values, setValues] = useState({ name: "", id: "" })
    const [isLoading, setIsLoading] = useState(false)

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            Preconditions.checkNotBlank(values.id, "Id")
            Preconditions.checkNotBlank(values.name, "Name")
            setIsLoading(true);
            const response = await fetchWithToken(`/api/v1/relation/workspace?workspaceId=${values.id}&name=${values.name}`, {
                method: "POST",
                body: tokenService.getUser().username
            });
            if (response.status === 200) {
                window.location.href = "/";
            } else {
                const error = await getBody(response);
                throw new Error(error);
            }
        } catch (error) {
            showMessage({
                message: error.message
            })
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
        <div className='center-screen'>
            <LoadingModal isLoading={isLoading} />                
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
