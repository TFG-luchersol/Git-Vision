import React, {useState} from 'react';
import '../App.css';
import '../static/css/home/home.css';
import { Alert, Button, Form, FormGroup, Input, Label, Modal } from 'reactstrap';
import tokenService from "../services/token.service.js";
import "../static/css/auth/authPage.css";
import { Link } from 'react-router-dom';
import InputWithIcon from '../components/InputWithIcon.js';
import { FaRegUserCircle, FaGithub } from "react-icons/fa";

export default function Login() {
    const userIcon = <FaRegUserCircle />
    const githubIcon = <FaGithub />

    const [message, setMessage] = useState(null)
    const [values, setValues] = useState({username: "", githubToken: ""})

    async function handleSubmit(event) {
        event.preventDefault()
        const reqBody = JSON.stringify(values);
        console.log(reqBody)
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
        const {name, value} = target;
        let newValues = { ...values };
        newValues[name] = value;
        setValues(newValues)
    }

    async function testDownload() {
        await fetch('/api/v1/github/TFG-luchersol/Git-Vision', {method: "POST"}).then(e => alert(`${e.status}`))
    }

    return (
        <div className="home-page-container">
            <button onClick={() => testDownload()} >DESCARGA DE PRUEBA</button>
                {/* <Alert isOpen={message} color="danger" style={{position:'absolute', top:'30px'}}>{message}</Alert>
                
                <Form onSubmit={handleSubmit} className='auth-form-container' >
                    <div style={{margin: "30px"}}>
                        <title className='center-title'>
                            <h1>Login</h1>
                        </title>

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
                            <Button type='submit'>Login</Button> 
                            <Button type='button'>
                                <Link className='custom-link' to={"/register"}>Registrer</Link>
                            </Button> 
                        </div>
                    </div>
                </Form> */}
                
            
        </div>
    );
}