import React, { useState } from 'react';
import '../../App.css';
import '../../static/css/home/home.css';
import { Alert, Button, Form, FormGroup, Input, Label, Modal } from 'reactstrap';
import tokenService from "../../services/token.service.js";
import "../../static/css/auth/authPage.css";
import { Link } from 'react-router-dom';
import CustomInput from '../../components/CustomInput.js';
import { FaRegUserCircle, FaGithub, FaLock, FaUnlock } from "react-icons/fa";

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

            if (response.status === 200) {
                const data = await response.json();
                tokenService.setLocalAccessToken(data.token)
                tokenService.setUser(data.user);
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

    async function testDownloadGithub() {
        await fetch('/api/v1/github/TFG-luchersol/Git-Vision', { method: "POST" }).then(e => alert(`GITHUB: ${e.status}`))
    }

    async function testDownloadClockify() {
        await fetch('/api/v1/clockify/workspaces/664b1bcbfae74255cbb0abc9?name=workspace_test_TFG', { method: "POST" }).then(e => alert(`CLOCKIFY: ${e.status}`))
    }

    return (
        <div className="home-page-container">
            {/* <div style={{display:'flex', flexDirection:'column'}}>
                <button onClick={() => testDownloadGithub()} >DESCARGA DE PRUEBA GITHUB</button>
                <button onClick={() => testDownloadClockify()} >DESCARGA DE PRUEBA CLOCKIFY</button>
            </div> */}

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