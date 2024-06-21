import React, {useState} from 'react';
import '../App.css';
import '../static/css/home/home.css';
import { Alert, Button, Form, FormGroup, Input, Label } from 'reactstrap';
import tokenService from "../services/token.service.js";
import "../static/css/auth/authButton.css";
import { Link } from 'react-router-dom';
import { IoPersonCircleOutline } from "react-icons/io5";
import "./home.css"

export default function Home() {
    const [message, setMessage] = useState(null)
    const [values, setValues] = useState({username: null, token: null})

    async function handleSubmit({ values }) {

        const reqBody = values;
        setMessage(null);
        await fetch("/api/v1/auth/signin", {
            headers: { "Content-Type": "application/json" },
            method: "POST",
            body: JSON.stringify(reqBody),
        })
            .then(function (response) {
                if (response.status === 200) return response.json();
                else return Promise.reject("Invalid login attempt");
            })
            .then(function (data) {
                tokenService.setUser(data);
                tokenService.updateLocalAccessToken(data.token);
                window.location.href = "/dashboard";
            })
            .catch((error) => {
                setMessage(error);
            });
    }

    function handleChange(event) {
        const target = event.target;
        const {name, value} = target;
        let newValues = { ...values };
        newValues[name] = value;
        setValues(newValues)
    }

    return (
        <div className="home-page-container">
            {message &&
                <Alert color="primary">{message}</Alert>
                }
            
                <Form onSubmit={handleSubmit} className='auth-form-container'>
                    <div style={{margin: "30px"}}>
                        <title className='center-title'>
                            <h1>Login</h1>
                        </title>
                        

                        <FormGroup>
                            <Label>Username:</Label>
                            <Input type='text' name='username' value={values.username || ""} onChange={handleChange}/>
                        </FormGroup>

                        <div className='button-group'>
                            <Button type='submit'>Login</Button> 
                            <Button type='button'>
                                <Link className='custom-link' to={"/user/sing-in"}>Registrer</Link>
                            </Button> 
                        </div>
                    </div>
                </Form>
                
            
        </div>
    );
}