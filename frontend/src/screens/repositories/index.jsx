import AccordionItem from "@components/AccordionItem.jsx";
import { useNotification } from "@context/NotificationContext";
import "@css";
import "@css/auth/authPage.css";
import "@css/home";
import "@css/repositories";
import tokenService from "@services/token.service.js";
import fetchWithToken from "@utils/fetchWithToken.ts";
import getBody from "@utils/getBody.ts";
import React, { useEffect, useState } from "react";
import { FaLink } from "react-icons/fa6";
import { IoLogoGithub } from "react-icons/io5";
import { SiClockify } from "react-icons/si";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "reactstrap";

export default function Repositories() {
  const { showMessage } = useNotification();
  const navigate = useNavigate();

  const [repositories, setRepositories] = useState({});
  const [workspaces, setWorkspaces] = useState({});
  const [relation, setRelation] = useState({});

  useEffect(() => {
    getRepositories();
    getWorkspaces();
    getRelation();
  }, []);

  const getRepositories = async () => {
    try {
      let newRepositories = await fetchWithToken("/api/v1/relation/repository");
      const repositories = await getBody(newRepositories);
      setRepositories(repositories);
    } catch (error) {
      showMessage({
        message: error.message
      })
    }
  };

  const getWorkspaces = async () => {
    try {
      let response = await fetchWithToken(`/api/v1/relation/workspace`);
      const workspaces = await getBody(response);
      setWorkspaces(workspaces);
    } catch (error) {
        showMessage({
            message: error.message
        })
    }
  };

  const getRelation = async () => {
    try {
      let response = await fetchWithToken(
        `/api/v1/relation/repository/workspace`
      );
      const workspace_repository = await getBody(response);
      setRelation(workspace_repository);
    } catch (error) {
        showMessage({
            message: error.message
        })
    }
  };

  function goToConfigurations(workspaceName) {
    if (relation[workspaceName]) {
      navigate(`/workspace/${workspaceName}`);
    }
  }

  return (
    <div style={{ display: "flex", justifyContent: "space-around" }}>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
        }}
      >
        <h1>
          Repositorios <IoLogoGithub />
        </h1>
        <div className="contenedor-rutas">
          {Object.keys(repositories).length > 0 ? (
            Object.keys(repositories).map((owner) => (
              <AccordionItem title={owner}>
                {repositories[owner].map((repo) => (
                  <div
                    onClick={() =>
                      (window.location.href = `/repository/${
                        owner + "/" + repo
                      }`)
                    }
                  >
                    <AccordionItem title={repo} leaf />
                  </div>
                ))}
              </AccordionItem>
            ))
          ) : (
            <h6 style={{ margin: 20 }}>NO HAY REPOSITORIOS DESCARGADOS</h6>
          )}
        </div>
        {tokenService.hasClockifyToken() && (
          <>
            <h1 style={{ marginTop: 10 }}>
              Workspace <SiClockify color="blue" />
            </h1>
            <div className="contenedor-rutas">
              {workspaces.length > 0 ? (
                workspaces.map((workspace) => (
                  <div
                    style={{ display: "flex", flexDirection: "row" }}
                    onClick={() => goToConfigurations(workspace.name)}
                  >
                    <div
                      className={relation[workspace.name] && "text-hover"}
                      style={{
                        display: "flex",
                        flexDirection: "row",
                        margin: "10px",
                        cursor: relation[workspace.name] && "pointer",
                      }}
                    >
                      <div>{workspace.name}</div>
                      {relation[workspace.name] && (
                        <>
                          <FaLink
                            style={{ marginInline: "15px", marginTop: "3px" }}
                          />
                          {relation[workspace.name].name}
                        </>
                      )}
                    </div>
                  </div>
                ))
              ) : (
                <h6 style={{ margin: 20 }}>NO HAY WORKSPACES DESCARGADOS</h6>
              )}
            </div>
          </>
        )}
      </div>

      <div
        className="button-group"
        style={{ justifyContent: "center", height: "70vh" }}
      >
        <Button>
          <Link className="custom-link" to={"/repository/download"}>
            Añadir repositorio
          </Link>
        </Button>

        {tokenService.hasClockifyToken() && (
          <>
            <Button>
              <Link className="custom-link" to={"/workspace/download"}>
                Añadir workspace
              </Link>
            </Button>
            <Button style={{ marginTop: 10 }}>
              <Link
                className="custom-link"
                to={"/linker/repository/workspace/"}
              >
                Enlazar proyecto con workspace
              </Link>
            </Button>
          </>
        )}
      </div>
    </div>
  );
}
