import CustomInput from '@components/CustomInput.jsx';
import "@css/workspace/details";
import fetchWithToken from '@utils/fetchWithToken.ts';
import React, { useEffect, useState } from 'react';
import { IoPersonCircleOutline } from 'react-icons/io5';
import { SiClockify } from "react-icons/si";
import { useParams } from 'react-router-dom';
import { Button } from 'reactstrap';

export default function Workspace() {
    const saveTokenGithubButton = <Button className='save-button' onClick={() => handleSave()}>Guardar</Button>;
    const clockifyIcon = <SiClockify />
    const [clockifyToken, setClockifyToken] = useState('');
    const {owner, repo} = useParams();
    useEffect(() => {
      getWorkspaceDetails()
    }, []);
  
    const getWorkspaceDetails = async () => {
  
    }
    
    const handleGithubTokenChange = (e) => setClockifyToken(e.target.value);
  
    const handleSave = async () => {
      await fetchWithToken(`/api/v1/relation/repository/${owner}/${repo}/token`, {
        method: "PUT", 
        body: clockifyToken
      }
      ).then(response => console.log(response));
  
    };

    return (<div className="details-container">
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
              icon={clockifyIcon}
              label={"Github Token*:"}
              type="text"
              id="github-token"
              value={clockifyToken}
              onChange={handleGithubTokenChange}
              button={saveTokenGithubButton}
            />
          </div>
        </div>);
}
