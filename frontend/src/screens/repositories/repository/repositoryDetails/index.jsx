import CustomInput from '@components/CustomInput.jsx';
import { useNotification } from '@context/NotificationContext';
import "@css/repositories/repository/repositoryDetails";
import fetchBackend from '@utils/fetchBackend.ts';
import getBody from '@utils/getBody';
import React, { useEffect, useState } from 'react';
import { FaGithub } from 'react-icons/fa';
import { IoPersonCircleOutline } from 'react-icons/io5';
import { LuSave } from "react-icons/lu";
import { useParams } from 'react-router-dom';

export default function RepositoryDetails() {
  const { showMessage } = useNotification();
  const saveTokenGithubButton = <LuSave className='save-button-repository-details' onClick={() => handleSave()} />;
  const githubIcon = <FaGithub />
  const [githubToken, setGithubToken] = useState('');
  const {owner, repo} = useParams();
  useEffect(() => {
    getRepositoryDetails()
  }, []);

  const getRepositoryDetails = async () => {
    // TODO: Completar esto y que se ponga la imagen
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
  );
}


