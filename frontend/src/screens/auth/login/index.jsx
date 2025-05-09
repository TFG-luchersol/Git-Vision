import CustomInput from '@components/CustomInput';
import { useNotification } from '@context/NotificationContext';
import '@css';
import "@css/auth/authPage.css";
import '@css/home';
import tokenService from "@services/token.service.js";
import fetchBackend from '@utils/fetchBackend';
import getBody from '@utils/getBody.ts';
import React, { useState } from 'react';
import { FaLock, FaRegUserCircle, FaUnlock } from "react-icons/fa";
import { Link, useNavigate } from 'react-router-dom';
import { Button, Form, FormGroup } from 'reactstrap';
 
export default function Login() {
    
    const navigate = useNavigate();
    const { showMessage } = useNotification();

    const userIcon = <FaRegUserCircle />
    const passwordLockIcon = <FaLock onClick={() => setShowPassword(prev => !prev)} />;
    const passwordUnlockIcon = <FaUnlock onClick={() => setShowPassword(prev => !prev)} />;

    const [values, setValues] = useState({ username: "", password: "" })
    const [showPassword, setShowPassword] = useState(false);

    async function handleSubmit(event) {
        event.preventDefault()
        const reqBody = JSON.stringify(values);
        try {
            const response = await fetchBackend("/api/v1/auth/signin", {
                headers: { 
                    "Content-Type": "application/json",
                },
                method: "POST",
                body: reqBody,
            });
            const jwt = await getBody(response);
            tokenService.setLocalAccessToken(jwt.token)
            tokenService.setUser(jwt.user);
            showMessage({
                message: "Credenciales correctas",
                type: "success"
            })
            await new Promise(resolve => setTimeout(resolve, 500));
            navigate(0);
        } catch (error) {
            showMessage({
                message: error.message
            })
            
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
            <Form onSubmit={handleSubmit} className='auth-form-container' >
                <div style={{ margin: "30px" }}>
                    <title className='center-title'>
                        <h1>Iniciar Sesión</h1>
                    </title>

                    <FormGroup>
                        <CustomInput
                            icon={userIcon}
                            label={"Usuario:"}
                            type='text'
                            name='username'
                            value={values.username || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <CustomInput
                            icon={showPassword ? passwordUnlockIcon : passwordLockIcon}
                            label={"Contraseña:"}
                            type={showPassword ? 'text' : 'password'}
                            name='password'
                            value={values.password || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>

                    <div className='button-group'>
                        <Button type='submit'>Aceptar</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/register"}>Registrarse</Link>
                        </Button>
                    </div>
                </div>
            </Form>
        </div>
    );
}
