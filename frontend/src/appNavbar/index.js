import React, { useState } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarToggler, Collapse} from 'reactstrap';
import { Link } from 'react-router-dom';
import { SiSwagger, SiGoogledocs } from "react-icons/si";
import UserInformation from './userInformation.js';

export default function AppNavbar() {

    const [collapsed, setCollapsed] = useState(true);
    const toggleNavbar = () => setCollapsed(!collapsed);

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
            </>
        )


    let userLogin = (
        <>
            <NavItem>
                <NavLink>
                    
                    <Link to={"/repositories"}>REPOSITORIES</Link>
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink>
                    <UserInformation/>
                </NavLink>
            </NavItem>
        </>
    )
    

    return (
        <div>
            <Navbar expand="md" style={{ backgroundColor:"#D7D0D0"}}>
                <NavbarBrand href="/">
                    <img alt="logo" src='/logo.png' style={{ height: 40, width: 140 }} />
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {user}
                        {userLogin}
                    </Nav>
                </Collapse>
            </Navbar>
            
        </div>
    );
}