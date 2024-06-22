import React, { useState, useEffect, useRef } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse, Popover, PopoverBody } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from '../services/token.service';
import jwt_decode from "jwt-decode";
import { SiSwagger, SiGoogledocs } from "react-icons/si";
import { IconBase } from 'react-icons/lib';
import UserInformation from './UserInformation.js';

export default function AppNavbar() {
    const [username, setUsername] = useState("");
    const jwt = null; // tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

    useEffect(() => {
        if (jwt) {
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])
    
   
    let user = (
            <>
                <NavItem>
                    <NavLink id="docs" tag={Link} to="/docs">
                        <SiGoogledocs title='Documentacion' style={{fontSize:50}}/>
                    </NavLink>
                </NavItem>
                <NavItem>
                    <NavLink id="plans" tag={Link} to="/swagger">
                        <SiSwagger title='Swagger' style={{fontSize:50}}/>
                    </NavLink>
                </NavItem>
                <NavbarText className="justify-content-end">{username}</NavbarText>
            </>
        )


    let userLogin = (
        <>
            <NavItem>
                <UserInformation/>
            </NavItem>
        </>
    )
    

    return (
        <div>
            <Navbar expand="md" style={{ backgroundColor:"#0000001c"}}>
                <NavbarBrand href="/">
                    <img alt="logo" src='/logo.png' style={{ height: 40, width: 140 }} />
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {user}
                        {tokenService.getUser()?.username}
                        {userLogin}
                    </Nav>
                </Collapse>
            </Navbar>
            
        </div>
    );
}