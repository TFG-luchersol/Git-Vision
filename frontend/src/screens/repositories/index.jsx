import AccordionItem from '@components/AccordionItem.jsx';
import '@css';
import '@css/auth/authPage.css';
import '@css/home';
import '@css/repositories';
import tokenService from "@services/token.service.js";
import fetchWithToken from '@utils/fetchWithToken.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { FaLink } from "react-icons/fa6";
import { IoLogoGithub } from "react-icons/io5";
import { SiClockify } from "react-icons/si";
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';

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
            let newRepositories = await fetchWithToken("/api/v1/relation/repository")
            const repositories = await getBody(newRepositories)
            setRepositories(repositories)
        } catch (e) {
            alert(e.message)
        }
    }

    const getWorkspaces = async () => {
        try {
            let response = await fetchWithToken(`/api/v1/relation/workspace/workspaces?userId=${tokenService.getUser().id}`)
            const workspaces = await getBody(response)
            setWorkspaces(workspaces)
        } catch (e) {
            
        }
    }

    const getRelation = async () => {
        try {
            let response = await fetchWithToken(`/api/v1/linker?userId=${tokenService.getUser().id}`)
            const workspace_repository = await getBody(response)
            setRelation(workspace_repository)
        } catch (e) {
            
        }
    }


    return (
        <div style={{ display: 'flex', justifyContent: 'space-around' }}>
            <div style={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
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
                {tokenService.hasClockifyToken() && <>    
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
                        <h6 style={{margin: 20}}>NO HAY WORKSPACES DESCARGADOS</h6>}
                </div>
                </>}
            </div>

            <div className='button-group' style={{ justifyContent: "center", height: "70vh" }}>
                <Button >
                    <Link className='custom-link' to={"/repository/download"}>Añadir repositorio</Link>
                </Button>
                
                {tokenService.hasClockifyToken() && <>
                    <Button >
                        <Link className='custom-link' to={"/workspace/download"}>Añadir workspace</Link>
                    </Button>
                    <Button style={{ marginTop: 10 }} >
                        <Link className='custom-link' to={"/linker/repository/workspace/"}>Enlazar proyecto con workspace</Link>
                    </Button>
                </>}
            </div>

        </div>
    );
}
