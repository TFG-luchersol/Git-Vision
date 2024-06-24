import React, { useState } from 'react';
import '../App.css';
import '../static/css/home/home.css';
import { Alert, Button, Form, FormGroup } from 'reactstrap';
import tokenService from "../services/token.service.js";
import '../static/css/auth/authPage.css'
import { Link } from 'react-router-dom';
import InputWithIcon from '../components/InputWithIcon.js';
import { FaRegUserCircle, FaGithub } from "react-icons/fa";

export default function Register() {

    const userIcon = <FaRegUserCircle />
    const githubIcon = <FaGithub />

    const [message, setMessage] = useState(null)
    const [values, setValues] = useState({ username: null, githubToken: null })

    async function handleSubmit() {
        const reqBody = values;
        setMessage(null);
        try {
            console.log(reqBody)
            const response = await fetch("/api/v1/auth/signup", {
                headers: { "Content-Type": "application/json" },
                method: "POST",
                body: JSON.stringify(reqBody),
            });

            if (response.status === 200) {
                const data = await response.json();
                tokenService.setUser(data);
                window.location.href = "/";
            } else {
                throw new Error("Invalid login attempt");
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
            {message &&
                <Alert color="primary" style={{ position: 'absolute', right: 30, top: 30 }}>{message}</Alert>
            }

            <Form onSubmit={handleSubmit} className='auth-form-container'>
                <div style={{ margin: "30px" }}>
                    <title className='center-title'><h1>Register</h1></title>
                    <FormGroup>
                        <InputWithIcon
                            icon={userIcon}
                            label={"Username:"}
                            type='text'
                            name='username'
                            value={values.username || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <InputWithIcon
                            icon={githubIcon}
                            label={"Github Token:"}
                            type='text'
                            name='githubToken'
                            value={values.githubToken || ""}
                            onChange={handleChange}
                        />
                    </FormGroup>
                    <div className='button-group'>
                        <Button type='submit'>Registrer</Button>
                        <Button type='button'>
                            <Link className='custom-link' to={"/"}>Cancelar</Link>
                        </Button>
                    </div>
                </div>
            </Form>


        </div>
    );
}