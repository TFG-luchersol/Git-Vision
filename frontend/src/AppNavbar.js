import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import { SiSwagger, SiGoogledocs } from "react-icons/si";
import { IoPersonCircleOutline } from "react-icons/io5";
import { IconBase } from 'react-icons/lib';
import UserInformation from './components/UserInformation';

function AppNavbar() {
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);
    const [visible, setVisible] = useState(true);

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

    function showInformation(){
        setVisible(state => !state)
    }

    let userLogin = (
        <>
            <NavItem>
                <IoPersonCircleOutline onClick={showInformation} style={{fontSize:60}}/>
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
            <UserInformation visibility={visible}/>
        </div>
    );
}

export default AppNavbar;