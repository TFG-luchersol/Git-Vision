import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import logo from "../src/static/images/logo.png";
import { SiSwagger, SiGoogledocs } from "react-icons/si";

function AppNavbar() {
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
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

    

    return (
        <div>
            <Navbar expand="md" style={{ backgroundColor:"#0000001c"}}>
                <NavbarBrand href="/">
                {/*/logo1-recortado.png  */}
                    <img alt="logo" src={logo} style={{ height: 40, width: 140 }} />
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {user}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}

export default AppNavbar;