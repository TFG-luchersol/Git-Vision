import CustomInput from '@components/CustomInput.jsx';
import "@css/repositories/repository/repositoryDetails";
import fetchWithToken from '@utils/fetchWithToken.ts';
import React, { useEffect, useState } from 'react';
import { FaGithub } from 'react-icons/fa';
import { IoPersonCircleOutline } from 'react-icons/io5';
import { useParams } from 'react-router-dom';
import { Button } from 'reactstrap';

export default function RepositoryDetails() {
  const saveTokenGithubButton = <Button className='save-button' onClick={() => handleSave()}>Guardar</Button>;
  const githubIcon = <FaGithub />
  const [githubToken, setGithubToken] = useState('');
  const {owner, repo} = useParams();
  useEffect(() => {
    getRepositoryDetails()
  }, []);

  const getRepositoryDetails = async () => {

  }
  
  const handleGithubTokenChange = (e) => setGithubToken(e.target.value);

  const handleSave = async () => {
    await fetchWithToken(`/api/v1/relation/user_repository/${owner}/${repo}/token`, {
      method: "PUT", 
      body: githubToken
    }
    ).then(response => console.log(response));

  };

  return (
    <div className="home-page-container">
    <div className="details-container">
      <div className="profile-container">
        <div className="profile-image">
          <img
            className='placeholder-image-url'
            style={{ height: 350, width: 350, borderRadius: "10px" }}
            src={'https://avatars.githubusercontent.com/u/172033719?s=400&v=4'}
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


