import CustomInput from '@components/CustomInput';
import DeleteModal from '@components/DeleteModal';
import { useNotification } from '@context/NotificationContext';
import '@css/details';
import tokenService from '@services/token.service.js';
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody.ts';
import React, { useEffect, useState } from 'react';
import { FaGithub, FaRegUserCircle } from "react-icons/fa";
import { LuSave } from "react-icons/lu";
import { MdDelete, MdOutlineEmail } from "react-icons/md";
import { SiClockify } from "react-icons/si";
import { Button } from 'reactstrap';

export default function Details() {
  const {showMessage} = useNotification();

  const userIcon = <FaRegUserCircle />
  const githubIcon = <FaGithub />
  const clockifyIcon = <SiClockify />
  const emailIcon = <MdOutlineEmail />
  const saveTokenGithubButton = <LuSave className='save-button-user-details' onClick={() => handleSave('github')} />;
  const deletedTokenClockifyButton = <MdDelete style={{fontSize: 30}} className='delete-button-user-details' onClick={() => setDeleteModalClockifyToken(true)}/>;
  const saveTokenClockifyButton = <LuSave className='save-button-user-details' onClick={() => handleSave('clockify')}/>;

  const clockifyButtons = <>
    {deletedTokenClockifyButton}
    {saveTokenClockifyButton}
  </>

  const [githubToken, setGithubToken] = useState('');
  const [clockifyToken, setClockifyToken] = useState('');
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState({});
  const [deleteModalAccount, setDeleteModalAccount] = useState(false);
  const [deleteModalClockifyToken, setDeleteModalClockifyToken] = useState(false);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const user = tokenService.getUser();
    setUserId(user?.id)
    setUsername(user?.username);
    setEmail(user?.email);
    setGithubToken(user?.githubToken);
    setClockifyToken(user?.clockifyToken);
  }, []);

  const handleGithubTokenChange = (e) => setGithubToken(e.target.value);
  const handleClockifyTokenChange = (e) => setClockifyToken(e.target.value);

  const handleSave = async (tokenType) => {
    const user = tokenService.getUser();
    try {
      if (tokenType === 'github') {
        const response = await fetchBackend('/api/v1/users/user/token/github', 
          {method: "PUT", body: githubToken}
        );
        const newGithubToken = await getBody(response)
        tokenService.setUser({...user, githubToken: newGithubToken})
        showMessage({
          message: "Token de GitHub actualizado",
          type: "info",
        })
      } else if (tokenType === 'clockify') {
        const response = await fetchBackend('/api/v1/users/user/token/clockify', 
          {method: "PUT", body: clockifyToken}
        );
        const newClockifyToken = await getBody(response)
        tokenService.setUser({...user, clockifyToken: newClockifyToken})
        showMessage({
          message: "Token de Clockify actualizado",
          type: "info",
        })
      }
    } catch (error) {
      showMessage({
        message: error.message
      })
    }
  };

  const handleDeleteClockifyToken = async () => {
    try {
      const response = await fetchBackend(`/api/v1/users/user/token/clockify`, {method: "DELETE"});
      const message = await getBody(response);
      tokenService.removeClockifyToken();
      setClockifyToken("");
      showMessage({
        message: message,
        type: "info"
      })
    } catch (error) {
      showMessage({
        message: error.message,
        type: "info"
      })
    } finally {
      setDeleteModalClockifyToken(false);
    }
  }

  const handleDeleteAccount = async () => {
    await fetchBackend(`/api/v1/users/${userId}`, {method: "DELETE"})
              .then(response => {
                setDeleteModalAccount(false)
                if(response.status === 200) {
                  tokenService.removeUser()
                  window.location.href = "/"
                } else {
                  showMessage({
                    message: "Error en borrado de usuario"
                  })
                }
              })
  };

  return (
    
    <div className="details-container">
      <DeleteModal 
          text='¿Quieres borrar tu cuenta?'
          handleDelete={handleDeleteAccount}
          isOpen={deleteModalAccount}
          onClose={() => setDeleteModalAccount(false)}
      />
      <DeleteModal 
        text='¿Quieres borrar tu token de clockify?'
        handleDelete={handleDeleteClockifyToken}
        isOpen={deleteModalClockifyToken}
        onClose={() => setDeleteModalClockifyToken(false)}
      />  
      <div className="profile-container">
        <div className="profile-image">
          <img
            className='placeholder-image-url'
            style={{ height: 350, width: 350 }}
            src={tokenService.getUser()?.avatarUrl}
            alt="Avatar"
            id='avatar'
          />
        </div>
        <div className="profile-info">
          <CustomInput
            icon={userIcon}
            label={"Username:"}
            type="text"
            id="username"
            value={username}
            readOnly
          />
          <CustomInput
            icon={emailIcon}
            label={"Email:"}
            type="email"
            id="email"
            value={email}
            readOnly
          />
          <Button className="delete-button-user-details" onClick={() => setDeleteModalAccount(true)}>Eliminar cuenta</Button>
        </div>
      </div>
      <div className="token-container">
        <CustomInput
          icon={githubIcon}
          label={"Github Token*:"}
          type="text"
          id="github-token"
          value={githubToken}
          onChange={handleGithubTokenChange}
          button={saveTokenGithubButton}
        />
        <CustomInput
          icon={clockifyIcon}
          label={"Clockify Token:"}
          type="text"
          id="clockify-token"
          value={clockifyToken}
          onChange={handleClockifyTokenChange}
          button={clockifyButtons}
        />
        <p className="note">
          Nota: Estos tokens serán utiliza por defecto a las horas de realizar las peticiones en Github o Clockify
          en caso de que los repositorios o espacios de trabajo respectivos no tengan un token predefinido asociado.
          A su vez, la existencia de un token en Github será presentada de forma obligatoria, mientras que el token de
          Clockify será opcional, siendo la única inconveniencia la inaccesibilidad a ciertas funciones.
        </p>
      </div>
    </div>
  );
}
