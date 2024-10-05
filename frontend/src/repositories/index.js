import React, { useState, useEffect, useRef } from 'react';
import '../App.css';
import '../static/css/home/home.css';
import tokenService from "../services/token.service.js";
import { Button } from 'reactstrap';
import '../static/css/auth/authPage.css'
import AccordionItem from '../components/AccordionItem.js'
import './repositories.css'
import { Link } from 'react-router-dom';
import { SiClockify } from "react-icons/si";
import { IoLogoGithub } from "react-icons/io5";
import { FaLink } from "react-icons/fa6";

export default function Repositories() {

    const [repositories, setRepositories] = useState({})
    const [workspaces, setWorkspaces] = useState({})

    useEffect(() => {
        getRepositories()
        getWorkspaces()
    }, [])

    const getRepositories = async () => {
        try {
            let newRepositories = await fetch(`/api/v1/relation/user_repository/repositories?userId=${tokenService.getUser().id}`)
            const json = await newRepositories.json()
            const repositories = json.information.information.repositories
            setRepositories(repositories)
        } catch (e) {
            alert(e)
        }
    }

    const getWorkspaces = async () => {
        try {
            let newWorkspaces = await fetch(`/api/v1/relation/user_workspace/workspaces?userId=${tokenService.getUser().id}`)
            const json = await newWorkspaces.json()
            const workspaces = json.information.information.workspaces
            setWorkspaces(workspaces)
        } catch (e) {
            alert(e)
        }
    }


    return (
        <div className='grey-cover'>
            <div style={{ zIndex: 200, display: 'flex', justifyContent: 'space-around', height: '100%' }}>

                <div style={{ position: 'relative', top: "100px" }}>
                    <h1>Repositorios <IoLogoGithub /></h1>
                    <div className='contenedor-rutas'>
                        {Object.keys(repositories).length > 0 ?
                            Object.keys(repositories).map(owner =>
                                <AccordionItem title={owner}>
                                    {repositories[owner].map(repo =>
                                        <div onClick={() => window.location.href = `/repository/${owner + "/" + repo}`}>
                                            <AccordionItem title={repo} leaf />
                                        </div>
                                    )
                                    }
                                </AccordionItem>
                            ) : <h6>NO HAY REPOSITORIOS DESCARGADOS</h6>
                        }
                    </div>
                    <h1 style={{ marginTop: 10 }}>Workspace <SiClockify color='blue' /></h1>
                    <div className='contenedor-rutas'>
                        {workspaces.length > 0 ?
                            workspaces.map(workspace => <AccordionItem leaf title={workspace.name} />) :
                            <h6>NO HAY REPOSITORIOS DESCARGADOS</h6>}
                    </div>
                </div>

                <div className='button-group' style={{ justifyContent: "center" }}>
                    <Button >
                        <Link className='custom-link' to={"/repository/download"}>Añadir repositorio</Link>
                    </Button>
                    <Button >
                        <Link className='custom-link' to={"/workspace/download"}>Añadir workspace</Link>
                    </Button>
                    <Button style={{ marginTop: 10 }} >
                        <Link className='custom-link' to={"/repository/workspace/linker"}>Enlazar proyecto con workspace</Link>
                    </Button>
                </div>

            </div>
        </div>
    );
}