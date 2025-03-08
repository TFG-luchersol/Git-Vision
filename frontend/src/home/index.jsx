import React from 'react';
import '../App.css';
import Login from '../auth/login/index.jsx';
import Repositories from '../repositories/index.js';
import tokenService from "../services/token.service.js";
import "../static/css/auth/authButton.css";
import '../static/css/home';

export default function Home() {

    return !tokenService.getUser() ? <Login/> : <Repositories/>;
    
}