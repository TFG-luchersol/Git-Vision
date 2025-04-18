import '@css';
import "@css/auth/authButton.css";
import '@css/home';
import tokenService from "@services/token.service.js";
import React from 'react';
import Login from '../auth/login/index.jsx';
import Repositories from '../repositories';

export default function Home() {

    return !tokenService.getUser() ? <Login/> : <Repositories/>;
    
}
