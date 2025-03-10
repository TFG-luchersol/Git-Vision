import CustomInput from '@components/CustomInput';
import '@css';
import "@css/auth/authPage.css";
import '@css/home';
import tokenService from "@services/token.service.js";
import getBody from '@utils/getBody.ts';
import React, { useState } from 'react';
import { FaLock, FaRegUserCircle, FaUnlock } from "react-icons/fa";
import { Link } from 'react-router-dom';
import { Alert, Button, Form, FormGroup } from 'reactstrap';

export default function Login() {
    const userIcon = <FaRegUserCircle />
    const passwordLockIcon = <FaLock onClick={() => setShowPassword(prev => !prev)} />;
    const passwordUnlockIcon = <FaUnlock onClick={() => setShowPassword(prev => !prev)} />;

    const [message, setMessage] = useState(null)
    const [values, setValues] = useState({ username: "", password: "" })
    const [showPassword, setShowPassword] = useState(false);


    async function handleSubmit(event) {
        event.preventDefault()
        const reqBody = JSON.stringify(values);
        setMessage(null);
        try {
            const response = await fetch("/api/v1/auth/signin", {
                headers: { "Content-Type": "application/json" },
                method: "POST",
                body: reqBody,
            });
            
            const {jwt} = await getBody(response);
            tokenService.setLocalAccessToken(jwt.token)
            tokenService.setUser(jwt.user);
            window.location.href = "/";
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

            <Form onSubmit={handleSubmit} className='auth-form-container' >
                <div style={{ margin: "30px" }}>
                    <title className='center-title'>
                        <h1>Login</h1>
                    </title>

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
                            icon={showPassword ? passwordUnlockIcon : passwordLockIcon}
                            label={"Password:"}
                            type={showPassword ? 'text' : 'password'}
                            name='password'
                            value={values.password || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>

                    <div className='button-group'>
                        <Button type='submit'>Login</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/register"}>Registrer</Link>
                        </Button>
                    </div>
                </div>
            </Form>
        </div>

    );
}