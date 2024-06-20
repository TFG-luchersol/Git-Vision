import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import { Button, Form, Input } from 'reactstrap';

export default function Home(){
    return(
        <div className="home-page-container">
            <div className="hero-div">
                <Form>
                    <Input title='Username' placeholder='username'/>
                    <Button>Iniciar sesion</Button>
                    <Button>Añadir cuenta</Button>
                </Form>           
            </div>
        </div>
    );
}