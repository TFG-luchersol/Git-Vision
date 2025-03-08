import React, { useState } from 'react';
import { IoPersonCircleOutline } from "react-icons/io5";
import { Link } from 'react-router-dom';
import { Popover, PopoverBody, PopoverHeader } from 'reactstrap';
import tokenService from '../services/token.service';
import "../static/css/appnavbar";

// eslint-disable-next-line react/no-typos
export default function UserInformation() {

  const [popoverOpen, setPopoverOpen] = useState(false);
  const togglePopover = () => setPopoverOpen(!popoverOpen);

  const user = tokenService.getUser();

  function sendLogoutRequest() {
    const user = tokenService.getUser();
    if (user !== null) {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }
  

  let logo = user ? <img
    className='circular-img'
    src={user?.avatarUrl}
    alt={<IoPersonCircleOutline id='avatarPopover' title='Avatar Error' style={{ color:'red', fontSize: 60 }} />}
    id='avatarPopover' 
  /> : <IoPersonCircleOutline id='avatarPopover' style={{ fontSize: 60, position: 'relative', bottom: 5 }} />;

  return (user && <>
    {logo}
    <Popover
      placement="bottom"
      isOpen={popoverOpen}
      target='avatarPopover'
      toggle={togglePopover}
    >
      <PopoverHeader>{user?.username}</PopoverHeader>
      <PopoverBody>
        <div className='information'>
          <Link to={'/details'} >Información de usuario</Link>
          <Link to={'/register'}>Añadir cuenta</Link>
          <hr/>
          <Link onClick={sendLogoutRequest} >Cerrar sesión</Link>
        </div>
      </PopoverBody>
    </Popover>
  </>
  );
}