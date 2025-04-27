import CustomInput from '@components/CustomInput.jsx';
import DeleteModal from '@components/DeleteModal';
import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/repositoryDetails";
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody';
import React, { useEffect, useState } from 'react';
import { FaGithub } from 'react-icons/fa';
import { IoMdRefresh } from 'react-icons/io';
import { IoPersonCircleOutline } from 'react-icons/io5';
import { LuSave } from "react-icons/lu";
import { useNavigate, useParams } from 'react-router-dom';
import { Button } from 'reactstrap';

export default function RepositoryDetails() {
  
  const { showMessage } = useNotification();
  const navigate = useNavigate();
  const {owner, repo} = useParams();

  const [githubToken, setGithubToken] = useState("");
  const [hasWorkspaceAsociate, setHasWorkspaceAsociate] = useState(true);
  const [urlAvatar, setUrlAvatar] = useState("");
  const [deleteModalRepository, setDeleteModalRepository] = useState(false);
  const [deleteModalRelationWorkspace, setDeleteModalRelationWorkspace] = useState(false);

  const saveTokenGithubButton = <LuSave className='save-button-repository-details' onClick={() => handleSave()} />;
  const githubIcon = <FaGithub />

  useEffect(() => {
    getRepositoryDetails()
  }, []);

  const getRepositoryDetails = async () => {
    try {
      const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}`);
      const repository = await getBody(response);
      setGithubToken(repository.token)
      setHasWorkspaceAsociate(!!repository.workspace)
      setUrlAvatar(repository.urlImagen)
    } catch (error) {
      showMessage({
        message: error.message
      })
    } 
  }

  const refreshImageUrl = async () => {
    try {
      const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/url_image/refresh`, {
        method: "PUT"
      });
      const imageUrl = await getBody(response);
      setUrlAvatar(imageUrl);
    } catch (error) {
      showMessage({
        message: error.message
      })
    } 
  }

  const handleDeleteRepository = async () => {
    try {
      const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}`, {method: "DELETE"});
      const message = await getBody(response);
      showMessage({
        message: message,
        type: "info"
      })
      await new Promise(resolve => setTimeout(resolve, 500));
      navigate("/");
    } catch (error) {
      showMessage({
        message: error.message,
        type: "info"
      })
    } finally {
      setDeleteModalRepository(false);
    }
  }

  const handleDeleteRelationWorkspace = async () => {
    try {
      const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/linker`, {method: "DELETE"});
      const message = await getBody(response);
      showMessage({
        message: message,
        type: "info"
      })
      await new Promise(resolve => setTimeout(resolve, 500));
      navigate("/");
    } catch (error) {
      showMessage({
        message: error.message,
        type: "info"
      })
    } finally {
      setDeleteModalRelationWorkspace(false);
    }
  }
  
  const handleGithubTokenChange = (e) => setGithubToken(e.target.value);

  const handleSave = async () => {
    try{
      const response = await fetchBackend(`/api/v1/relation/repository/${owner}/${repo}/token`, {
          method: "PUT", 
          body: githubToken
        }
      )
      await getBody(response);
  } catch (error) {
    showMessage({
        message: error.message
    })
}
  };

  return (
    <div className="details-container">
      <DeleteModal 
        text='¿Estas seguro de querer borrar este repositorio?'
        subtitle="Se borrará toda configuración realizada y la relación a un workspace en caso de existir"
        handleDelete={handleDeleteRepository}
        isOpen={deleteModalRepository}
        onClose={setDeleteModalRepository}
      />
      <DeleteModal 
        text='¿Estas seguro de querer borrar la relación a este workspace?'
        subtitle="Se borrará toda configuración realizada en dicho workspace"
        handleDelete={handleDeleteRelationWorkspace}
        isOpen={deleteModalRelationWorkspace}
        onClose={setDeleteModalRelationWorkspace}
      />
      <div className="profile-container">
        <div className="profile-image">
          <div >
            <IoMdRefresh onClick={refreshImageUrl} className="refresh-icon-repository-details" />
          </div>
          {
            urlAvatar ?
            <img
              className='placeholder-image-url'
              style={{ height: 350, width: 350, borderRadius: "10px" }}
              src={urlAvatar}
              alt="Avatar"
              id='avatar'
            /> :
            <IoPersonCircleOutline id='avatar' title='Avatar Error' style={{ color: 'red', height: 350, width: 350, }} />
          }
          
        </div>
        <Button className='delete-button-repository-details' 
                onClick={() => setDeleteModalRepository(true)} 
                style={{width: "100%"}}>
          Borrar repositorio
        </Button>
        {hasWorkspaceAsociate &&
        <Button className='delete-button-repository-details' 
                onClick={() => setDeleteModalRelationWorkspace(true)} 
                style={{width: "100%"}}>
          Borrar relacion con workspace
        </Button>}
      </div>
      <div className="token-container">
        <CustomInput
          icon={githubIcon}
          label={"Github Token:"}
          type="text"
          id="github-token"
          value={githubToken}
          onChange={handleGithubTokenChange}
          button={saveTokenGithubButton}
        />
      </div>
    </div>
  );
}


