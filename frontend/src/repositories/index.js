import React, { useEffect, useState } from 'react';
import { FaLink } from "react-icons/fa6";
import { IoLogoGithub } from "react-icons/io5";
import { SiClockify } from "react-icons/si";
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
import '../App.css';
import AccordionItem from '../components/AccordionItem.js';
import tokenService from "../services/token.service.js";
import '../static/css/auth/authPage.css';
import '../static/css/home/home.css';
import fetchWithToken from '../util/fetchWithToken.ts';
import getBody from '../util/getBody.ts';
import './repositories.css';

export default function Repositories() {

    const [repositories, setRepositories] = useState({})
    const [workspaces, setWorkspaces] = useState({})
    const [relation, setRelation] = useState({})

    useEffect(() => {
        getRepositories();
        getWorkspaces();
        getRelation();
    }, [])

    const getRepositories = async () => {
        try {
            let newRepositories = await fetchWithToken("/api/v1/relation/user_repository/repositories")
            const {repositories} = await getBody(newRepositories)
            setRepositories(repositories)
        } catch (e) {
            alert(e.message)
        }
    }

    const getWorkspaces = async () => {
        try {
            let newWorkspaces = await fetchWithToken(`/api/v1/relation/user_workspace/workspaces?userId=${tokenService.getUser().id}`)
            const {workspaces} = await getBody(newWorkspaces)
            setWorkspaces(workspaces)
        } catch (e) {
            // alert(e)
        }
    }

    const getRelation = async () => {
        try {
            let newRelation = await fetchWithToken(`/api/v1/linker?userId=${tokenService.getUser().id}`)
            const {workspace_repository} = await getBody(newRelation)
            setRelation(workspace_repository)
        } catch (e) {
            // alert(e)
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
                            ) : <h6 style={{margin: 20}} >NO HAY REPOSITORIOS DESCARGADOS</h6>
                        }
                    </div>
                    <h1 style={{ marginTop: 10 }}>Workspace <SiClockify color='blue' /></h1>
                    <div className='contenedor-rutas'>
                        {workspaces.length > 0 ?
                            workspaces.map(workspace => 
                                    <div style={{display: "flex", flexDirection: "row"}}>
                                        <AccordionItem leaf title={workspace.name} />
                                        <div style={{display: "flex", flexDirection: "column"}}>
                                        {relation[workspace.name] && 
                                            relation[workspace.name].map(r => 
                                                <div style={{ marginLeft: '20px', marginTop: '10px' }}>
                                                    <FaLink style={{marginRight: "20px"}}/>{r}
                                                </div>
                                            )
                                        }
                                        </div>
                                            
                                    </div>
                                ) :
                            <h6 style={{margin: 20}}>NO HAY REPOSITORIOS DESCARGADOS</h6>}
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