import React, { useEffect, useState } from 'react';
import { FaGithub, FaRegUserCircle } from "react-icons/fa";
import { IoPersonCircleOutline } from 'react-icons/io5';
import { MdOutlineEmail } from "react-icons/md";
import { SiClockify } from "react-icons/si";
import { Button } from 'reactstrap';
import InputWithIcon from '../components/InputWithIcon';
import tokenService from '../services/token.service.js'
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
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');

  useEffect(() => {
    const user = tokenService.getUser();
    setUsername(user?.username);
    setEmail(user?.email);
    setGithubToken(user?.githubToken);
    setClockifyToken(user?.clockifyToken)
  }, []);

  const handleGithubTokenChange = (e) => setGithubToken(e.target.value);
  const handleClockifyTokenChange = (e) => setClockifyToken(e.target.value);

  const handleSave = (tokenType) => {
    if (tokenType === 'github') {
      console.log('Saving Github Token:', githubToken);
    } else if (tokenType === 'clockify') {
      console.log('Saving Clockify Token:', clockifyToken);
    }
  };

  const handleDeleteAccount = () => {
    console.log('Account deleted');
  };

  return (
    <div className="App">
      <div className="profile-container">
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
          <InputWithIcon
            icon={userIcon}
            label={"Username:"}
            type="text"
            id="username"
            value={username}
            readOnly
          />
          <InputWithIcon
            icon={emailIcon}
            label={"Email:"}
            type="email"
            id="email"
            value={email}
            readOnly
          />
          <Button className="delete-button" onClick={handleDeleteAccount}>Eliminar cuenta</Button>
        </div>
      </div>
      <div className="token-container">
        <InputWithIcon
          icon={githubIcon}
          label={"Github Token*:"}
          type="text"
          id="github-token"
          value={githubToken}
          onChange={handleGithubTokenChange}
          button={saveTokenGithubButton}
        />
        <InputWithIcon
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
  );
}
