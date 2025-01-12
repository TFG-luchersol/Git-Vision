import React, { useEffect, useState } from 'react';
import { IoPersonCircleOutline } from 'react-icons/io5';
import { Button } from 'reactstrap';
import CustomInput from '../../../components/CustomInput.js'
import tokenService from '../../../services/token.service.js'
import './details.css';
import { FaGithub } from 'react-icons/fa';

export default function RepositoryDetails() {
  const saveTokenGithubButton = <Button className='save-button' onClick={() => handleSave('github')}>Guardar</Button>;
  const githubIcon = <FaGithub />
  const [githubToken, setGithubToken] = useState('');

  useEffect(() => {
    getRepositoryDetails()
  }, []);

  const getRepositoryDetails = async () => {

  }
  const handleGithubTokenChange = (e) => setGithubToken(e.target.value);

  const handleSave = async (tokenType) => {
    const user = tokenService.getUser();
    const response = await fetch(`/api/v1/relation/user_repository/${user.username}/token/github?login`, 
      {method: "PUT", body: githubToken}
    );
  };

  return (
    <div className="home-page-container">
    <div className="details-container">
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
      </div>
    </div>
    </div>
  );
}


