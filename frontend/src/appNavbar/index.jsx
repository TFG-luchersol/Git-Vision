import React, { useEffect, useState } from "react";
import { RiGitRepositoryLine } from "react-icons/ri";
import { SiGoogledocs, SiSwagger } from "react-icons/si";
import { Link, useLocation, useParams } from "react-router-dom";
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink } from "reactstrap";
import logo from "../../public/logo.png";
import tokenService from "../services/token.service.js";
import UserInformation from "./UserInformation";

export default function AppNavbar() {
    const { owner: paramOwner, repo: paramRepo } = useParams();
    const location = useLocation();
    const [owner, setOwner] = useState(paramOwner || "");
    const [repo, setRepo] = useState(paramRepo || "");
    const [collapsed, setCollapsed] = useState(true);

    // Actualizar owner y repo cuando la URL cambie
    useEffect(() => {
        const pathParts = location.pathname.split("/");
        if (pathParts.length >= 3 && pathParts[1] === "repository") {
            setOwner(pathParts[2]);
            setRepo(pathParts[3] || "");
        } else {
            setOwner("");
            setRepo("");
        }
    }, [location]);

    const toggleNavbar = () => setCollapsed(!collapsed);

    let user = (
        <>
            <NavItem>
                <NavLink id="docs" tag={Link} to="/docs">
                    <SiGoogledocs title="Documentación" style={{ fontSize: 50 }} />
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink id="plans" tag={Link} to="/swagger">
                    <SiSwagger title="Swagger" style={{ fontSize: 50 }} />
                </NavLink>
            </NavItem>
        </>
    );

    let userLogin = (
        <>
            <NavItem>
                <NavLink id="repositories" tag={Link} to="/repositories">
                    <RiGitRepositoryLine style={{ fontSize: 50 }} />
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink>
                    <UserInformation />
                </NavLink>
            </NavItem>
        </>
    );

    return (
        <div>
            <Navbar expand="md" style={{ backgroundColor: "#D7D0D0" }}>
                <NavbarBrand href="/">
                    <img alt="logo" src={logo} style={{ height: 40, width: 140 }} />
                </NavbarBrand>
                {
                    owner && repo && 
                    <NavItem className="repo-box">
                        <NavLink id="repo" tag={Link} to={`/repository/${owner}/${repo}`}>
                        {`${owner}/${repo}`}
                        </NavLink>
                    </NavItem>
                }
                
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {user}
                        {tokenService.getUser() && userLogin}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}
