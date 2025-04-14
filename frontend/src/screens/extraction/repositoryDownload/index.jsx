import AlertMessage from '@components/AlertMessage';
import CustomInput from '@components/CustomInput';
import LoadingModal from '@components/LoadingModal';
import '@css';
import "@css/auth/authPage.css";
import '@css/home';
import Preconditions from '@utils/check.js';
import fetchWithToken from '@utils/fetchWithToken.ts';
import React, { useState } from 'react';
import { FaGithub, FaRegUserCircle } from "react-icons/fa";
import { Link } from 'react-router-dom';
import { Button, Form, FormGroup, Input, Label } from 'reactstrap';

export default function RepositoryDownload() {
    const userIcon = <FaRegUserCircle />
    const githubIcon = <FaGithub />
    
    const [message, setMessage] = useState(null);
    const [values, setValues] = useState({owner: "", repo: "", token: "" });
    const [validateToken, setValidateToken] = useState(false);
    const [isLoading, setIsLoading] = useState(false)

    async function handleSubmit(event) {
        event.preventDefault()
        
        setMessage(null);
        try {
            Preconditions.checkNotBlank(values.owner, "Owner");
            Preconditions.checkNotBlank(values.repo, "Repository");
            Preconditions.if(validateToken).checkNotBlank(values.token, "Token");
            let url = `/api/v1/relation/repository/${values.owner}/${values.repo}`
            if(values.validOtherToken) 
                url += `?token=${values.token}`;
            
            setIsLoading(true);
            const response = await fetchWithToken(url, {
                method: "POST",
                headers: { 
                    "Content-Type": "application/json"
                }
            }
            );
            if (response.status === 200) {
                window.location.href = "/";
            } else {
                const error = await response.json();
                setMessage(error.message)
            }
        } catch (error) {
            setMessage(error.message);
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
            <AlertMessage message={message}/>

            <Form onSubmit={handleSubmit} className='auth-form-container' >
                <div style={{ margin: "30px" }}>
                    <title className='center-title'>
                        <h1>Download Repository</h1>
                    </title>

                    <FormGroup>
                        <CustomInput
                            icon={userIcon}
                            label={"Owner:"}
                            type='text'
                            name='owner'
                            value={values.owner}
                            onChange={handleChange}
                        />
                        <CustomInput
                            label={"Repository:"}
                            type='text'
                            name='repo'
                            value={values.repo}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <CustomInput
                            icon={githubIcon}
                            label={"Github Token:"}
                            type='text'
                            name='token'
                            value={values.token}
                            onChange={handleChange}
                        />
                        <div style={{marginTop: 10}}>
                            <Label style={{marginRight: 10}}>Use new token: </Label>
                            <Input
                                type='switch'
                                value={validateToken}
                                onChange={() => setValidateToken(!validateToken)}
                            />
                        </div>
                    </FormGroup>

                    <div className='button-group'>
                        <Button type='submit'>Download</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/"}>Cancel</Link>
                        </Button>
                    </div>
                </div>
            </Form>

        </div>
    );
};
