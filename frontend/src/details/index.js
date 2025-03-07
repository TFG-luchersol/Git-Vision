import React, { useEffect, useState } from 'react';
import { FaGithub, FaRegUserCircle } from "react-icons/fa";
import { IoPersonCircleOutline } from 'react-icons/io5';
import { MdOutlineEmail } from "react-icons/md";
import { SiClockify } from "react-icons/si";
import { Button, Modal, ModalBody, ModalHeader } from 'reactstrap';
import CustomInput from '../components/CustomInput.js';
import tokenService from '../services/token.service.js';
import fetchWithToken from '../util/fetchWithToken.ts';
import getBody from '../util/getBody.ts';
import './details.css';

export default function Details() {
  const userIcon = <FaRegUserCircle />
  const githubIcon = <FaGithub />
  const clockifyIcon = <SiClockify />
  const emailIcon = <MdOutlineEmail />
  const saveTokenGithubButton = <Button className='save-button' onClick={() => handleSave('github')}>Guardar</Button>;
  const saveTokenClockifyButton = <Button className='save-button' onClick={() => handleSave('clockify')}>Guardar</Button>;

  const [githubToken, setGithubToken] = useState('');
  const [clockifyToken, setClockifyToken] = useState('');
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState({});
  const [deleteModal, setDeleteModal] = useState(false);
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
        const response = await fetchWithToken(`/api/v1/users/user/${username}/token/github`, 
          {method: "PUT", body: githubToken}
        );
        const result = await getBody(response)
        tokenService.setUser({...user, githubToken: result.githubToken})
      } else if (tokenType === 'clockify') {
        const response = await fetchWithToken(`/api/v1/users/user/${username}/token/clockify`, 
          {method: "PUT", body: clockifyToken}
        );
        const result = await getBody(response)
        tokenService.setUser({...user, clockifyToken: result.clockifyToken})
      }
    } catch (error) {
      alert(error)
    }
  };

  const handleDeleteAccount = async () => {
    await fetchWithToken(`/api/v1/users/${userId}`, {method: "DELETE"})
              .then(response => {
                setDeleteModal(false)
                if(response.status === 200) {
                  tokenService.removeUser()
                  window.location.href = "/"
                } else {
                  alert("Error en borrado de usuario")
                }
              })
  };

  return (
    <div className="home-page-container">
    <div className="details-container">
      <div className="profile-container">
        <Modal style={{position:'absolute', top: "50%", left: "50%", transform: "translate(-50%, -50%)", padding: "20px"}} isOpen={deleteModal}>
          <ModalHeader>¿Quieres borrar tu cuenta?</ModalHeader>
          <ModalBody style={{display: "flex", justifyContent: "space-around"}}>
            <Button color="primary" onClick={handleDeleteAccount}>SI</Button>
            <Button color="danger" onClick={() => setDeleteModal(false)}>NO</Button>
          </ModalBody>
        </Modal>
        <div className="profile-image">
          <img
            className='placeholder-image-url'
            style={{ height: 350, width: 350 }}
            src={'https://avatars.githubusercontent.com/u/93008812?v=4'}
            alt={<IoPersonCircleOutline id='avatar' title='Avatar Error' style={{ color: 'red', fontSize: 60 }} />}
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
          <Button className="delete-button" onClick={() => setDeleteModal(true)}>Eliminar cuenta</Button>
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
          button={saveTokenClockifyButton}
        />
        <p className="note">
          Nota: Estos tokens serán utiliza por defecto a las horas de realizar las peticiones en Github o Clockify
          en caso de que los repositorios o espacios de trabajo respectivos no tengan un token predefinido asociado.
          A su vez, la existencia de un token en Github será presentada de forma obligatoria, mientras que el token de
          Clockify será opcional, siendo la única inconveniencia la inaccesibilidad a ciertas funciones.
        </p>
      </div>
    </div>
    </div>
  );
}
