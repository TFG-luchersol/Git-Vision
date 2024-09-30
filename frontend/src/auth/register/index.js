import React, { useState } from 'react';
import { FaEnvelope, FaGithub, FaLock, FaRegUserCircle, FaUnlock } from "react-icons/fa";
import { Link } from 'react-router-dom';
import { Alert, Button, Form, FormGroup } from 'reactstrap';
import '../../App.css';
import CustomInput from '../../components/CustomInput.js';
import tokenService from "../../services/token.service.js";
import '../../static/css/auth/authPage.css';
import '../../static/css/home/home.css';
import Preconditions from '../../util/check.js';

export default function Register() {

    const userIcon = <FaRegUserCircle />;
    const githubIcon = <FaGithub />;
    const emailIcon = <FaEnvelope />;
    const passwordLockIcon = <FaLock onClick={() => setShowPassword(prev => !prev)} />;
    const passwordUnlockIcon = <FaUnlock onClick={() => setShowPassword(prev => !prev)} />;

    const [message, setMessage] = useState(null)
    const [values, setValues] = useState({ username: "", email: "", password: "", githubToken: "" })
    const [showPassword, setShowPassword] = useState(false);

    async function handleSubmit(event) {
        event.preventDefault()
        const reqBody = JSON.stringify(values);
        setMessage(null);
        try {
            const dataRegister = await fetch("/api/v1/auth/signup", {
                headers: { "Content-Type": "application/json" },
                method: "POST",
                body: reqBody,
            }).then(response => response.json());
            if(dataRegister.status !== 200) { 
                // console.log(dataRegister)
                // if(dataRegister.errors && dataRegister.errors?.length !== 0){
                //     const firstError = dataRegister.errors[0]
                //     const {field, defaultMessage} = firstError
                //     throw Error(`${field.charAt(0).toUpperCase() + field.slice(1)} ${defaultMessage}`);
                // }
                throw Error(dataRegister.message)
            } else {
                await fetch("/api/v1/auth/signin", {
                    headers: { "Content-Type": "application/json" },
                    method: "POST",
                    body: reqBody,
                })
                    .then(response => response.json())
                    .then(function (data) {
                        if (data.status !== "200") {
                            setMessage(data.message);
                        } else {
                            tokenService.setUser(data.user);
                            tokenService.updateLocalAccessToken(data.token);
                            window.location.href = "/";
                        }
                    })
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
            <Alert isOpen={message} color="danger" style={{ position: 'fixed', top: '15%' }}>{message}</Alert>
            <Form style={{marginTop: "20%"}} onSubmit={handleSubmit} className='auth-form-container'>
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