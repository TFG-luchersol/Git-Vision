import React from 'react';
import '../App.css';
import '../static/css/home/home.css';
import tokenService from "../services/token.service.js";
import "../static/css/auth/authButton.css";
import Login from '../auth/login';

export default function Home() {

    return (
        !tokenService.getUser() ? <Login/> :
        <div className="home-page-container">
        </div> 
    );
}