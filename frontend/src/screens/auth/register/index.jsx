import CustomInput from '@components/CustomInput';
import { useNotification } from '@context/NotificationContext';
import '@css';
import '@css/auth/authPage.css';
import '@css/home';
import tokenService from "@services/token.service.js";
import fetchBackend from '@utils/fetchBackend';
import getBody from '@utils/getBody';
import React, { useState } from 'react';
import { FaEnvelope, FaGithub, FaLock, FaRegUserCircle, FaUnlock } from "react-icons/fa";
import { Link } from 'react-router-dom';
import { Button, Form, FormGroup } from 'reactstrap';


export default function Register() {
    const { showMessage } = useNotification();

    const userIcon = <FaRegUserCircle />;
    const githubIcon = <FaGithub />;
    const emailIcon = <FaEnvelope />;
    const passwordLockIcon = <FaLock onClick={() => setShowPassword(prev => !prev)} />;
    const passwordUnlockIcon = <FaUnlock onClick={() => setShowPassword(prev => !prev)} />;

    const [values, setValues] = useState({ username: "", email: "", password: "", githubToken: "" })
    const [showPassword, setShowPassword] = useState(false);

    async function handleSubmit(event) {
        event.preventDefault()
        const reqBodySignup = JSON.stringify(values);
        const reqBodySignin = JSON.stringify({username: values.username, password: values.password});
        try {
            const dataRegister = await fetchBackend("/api/v1/auth/signup", {
                headers: { "Content-Type": "application/json" },
                method: "POST",
                body: reqBodySignup,
            });
            if(dataRegister.status !== 200) { 
                const message = await getBody(dataRegister)
                throw Error(message)
            } else {
                const dataSignin = await fetchBackend("/api/v1/auth/signin", {
                    headers: { "Content-Type": "application/json" },
                    method: "POST",
                    body: reqBodySignin,
                });
                if (dataSignin.status !== 200) {
                    const message = await getBody(dataSignin)
                    throw Error(message)
                } else {
                    const response = await getBody(dataSignin)
                    tokenService.setUser(response.user);
                    tokenService.updateLocalAccessToken(response.token);
                    window.location.href = "/";
                }
            }
        } catch (error) {
            showMessage({
                message: error.message
            });
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
            <Form onSubmit={handleSubmit} className='auth-form-container'>
                <div style={{ margin: "30px" }}>
                    <title className='center-title'><h1>Register</h1></title>
                    <FormGroup>
                        <CustomInput
                            icon={userIcon}
                            label={"Username:"}
                            type='text'
                            name='username'
                            value={values.username || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <CustomInput
                            icon={githubIcon}
                            label={"Github Token:"}
                            type='text'
                            name='githubToken'
                            value={values.githubToken || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <CustomInput
                            icon={emailIcon}
                            label={"Email:"}
                            type='email'
                            name='email'
                            value={values.email || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <CustomInput
                            icon={showPassword ? passwordUnlockIcon : passwordLockIcon}
                            label={"Password:"}
                            type={showPassword ? 'text' : 'password'}
                            name='password'
                            value={values.password || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <div className='button-group'>
                        <Button type='submit'>Register</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/"}>Cancelar</Link>
                        </Button>
                    </div>
                </div>
            </Form>


        </div>
    );
}
