import React, { useState, useEffect, useRef } from 'react';
import '../App.css';
import '../static/css/home/home.css';
import tokenService from "../services/token.service.js";
import { Button } from 'reactstrap';
import '../static/css/auth/authPage.css'
import AccordionItem from '../components/AccordionItem.js'
import './repositories.css'
import { Link } from 'react-router-dom';

export default function Repositories() {

    const [repositories, setRepositories] = useState({})
    const [workspaces, setWorkspaces] = useState({})

    useEffect(() => {
        getRepositories()
        getWorkspaces()
    }, [])

    const getRepositories = async () => {
        // try {
        //     let newRepositories = await fetch(`/api/v1/relation/user_repository/repositories?userId=${1}`)
        //     const json = await newRepositories.json()
        //     const repositories = json.data.information.repositories
        //     setRepositories(repositories)
        // } catch (e) {
        //     alert(e)
        // }
    }

    const getWorkspaces = async () => {
        // try {
        //     let newWorkspaces = await fetch(`/api/v1/relation/user_repository/repositories?userId=${1}`)
        //     newWorkspaces = await newWorkspaces.json()
        //     setWorkspaces(newWorkspaces)
        // } catch(e) {
        //     alert(e)
        // }
    }


    return (
        <div className='grey-cover'>
        <div style={{zIndex:200, display: 'flex', justifyContent: 'center', height: '100%' }}>

            <div>
                <h1>Repositorios 📂</h1>
                {Object.keys(repositories).length > 0 ?
                    Object.keys(repositories).map(owner =>
                        <AccordionItem title={owner}>
                            {repositories[owner].map(repo =>
                                <AccordionItem onClick={() => window.location.href = '/files/1'} title={repo} leaf>
                                    <span>📄</span> <span>📋</span>
                                </AccordionItem>
                            )
                            }
                        </AccordionItem>
                    ) : <h6>NO HAY REPOSITORIOS DESCARGADOS</h6>
}

                <h2>Workspace ⏲️</h2>
                {workspaces.size > 0 ?
                    workspaces.keys().map(owner => {
                        return (<AccordionItem title={owner}>
                            {workspaces.get(owner).map(repo =>
                                <AccordionItem title={repo} leaf>
                                    <span>📄</span> <span>📋</span>
                                </AccordionItem>)
                            }
                        </AccordionItem>);
                    }) : <h6>NO HAY REPOSITORIOS DESCARGADOS</h6>}
            </div>

            <div className='button-group'>
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

        </div></div>
    );
}