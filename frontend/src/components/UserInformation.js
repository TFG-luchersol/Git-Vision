import React, {useState} from 'react';
import "./index.css";
import { NavLink, Navbar, NavbarBrand, Row, Table } from 'reactstrap';
import tokenService from '../services/token.service';
import { Link } from 'react-router-dom';


export default function UserInformation({visibility}) {

    function sendLogoutRequest() {
        const jwt = window.localStorage.getItem("jwt");
        if (jwt || typeof jwt === "undefined") {
          tokenService.removeUser();
          window.location.href = "/";
        } else {
          alert("There is no user logged in");
        }
      }

    return (visibility &&
        <div style={{display:'flex', justifyContent:'flex-end'}}>
             <div className='user-information'>
                <Link className='custom-link' to={'/details'} >Información de usuario</Link>
                <Link className='custom-link' to={'/registrer'}>Añadir cuenta</Link>
                <hr></hr>
                <Link className='custom-link' onClick={sendLogoutRequest} >Cerrar sesión</Link>
            </div>
        </div>
        
    );
}