import React, { useState } from 'react';
import './details.css';
import { IoPersonCircleOutline } from 'react-icons/io5';

export default function Details() {
  const [githubToken, setGithubToken] = useState('');
  const [clockifyToken, setClockifyToken] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');

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
            alt={<IoPersonCircleOutline id='avatar' title='Avatar Error' style={{ color:'red', fontSize: 60 }} />}
            id='avatar' 
        />
        </div>
        <div className="profile-info">
          <div className="input-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
            />
          </div>
          <div className="input-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
            />
          </div>
          <button className="delete-account" onClick={handleDeleteAccount}>
            Eliminar cuenta
          </button>
        </div>
      </div>
      <div className="token-container">
        <div className="input-group">
          <label htmlFor="github-token">Token Github *</label>
          <input
            type="text"
            id="github-token"
            value={githubToken}
            onChange={handleGithubTokenChange}
          />
          <button onClick={() => handleSave('github')}>Guardar</button>
        </div>
        <div className="input-group">
          <label htmlFor="clockify-token">Token Clockify</label>
          <input
            type="text"
            id="clockify-token"
            value={clockifyToken}
            onChange={handleClockifyTokenChange}
          />
          <button onClick={() => handleSave('clockify')}>Guardar</button>
        </div>
        <p className="note">
          Nota: Estos tokens serán utiliza por defecto a las horas de realizar las peticiones en Github o Clockify en caso de que los repositorios o espacios de trabajo respectivos no tengan un token predefinido asociado. A su vez, la existencia de un token en Github será presentada de forma obligatoria, mientras que el token de Clockify se presentará como opcional, siendo la única inconveniencia la inaccesibilidad a ciertas funciones.
        </p>
      </div>
    </div>
  );
}
