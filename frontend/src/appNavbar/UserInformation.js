import React, {useState} from 'react';
import "../static/css/appnavbar/appnavbar.css";
import { Popover, PopoverBody } from 'reactstrap';
import tokenService from '../services/token.service';
import { Link } from 'react-router-dom';
import { IoPersonCircleOutline } from "react-icons/io5";

// eslint-disable-next-line react/no-typos
export default function UserInformation() {

  const [popoverOpen, setPopoverOpen] = useState(false);
  const togglePopover = () => setPopoverOpen(!popoverOpen);

  const user = tokenService.getUser();
  function sendLogoutRequest() {
    if (user || typeof user === "undefined") {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }
  
  
  let logo = user ? <img
      className='circular-img'
      src={'https://avatars.githubusercontent.com/u/93008812?v=4'}
      alt='User Avatar'
      id='avatarPopover' /> :
    <IoPersonCircleOutline
      style={{ fontSize: 60 }}
      alt='User Avatar'
      id='avatarPopover'
    />
    ;

  return (<>
    {logo}
    <Popover
      placement="bottom"
      isOpen={popoverOpen}
      target='avatarPopover'
      toggle={togglePopover}
    >
      <PopoverBody>
        <div className='information'>
          <Link to={'/details'} >Información de usuario</Link>
          <Link to={'/registrer'}>Añadir cuenta</Link>
          <hr/>
          <Link onClick={sendLogoutRequest} >Cerrar sesión</Link>
        </div>
      </PopoverBody>
    </Popover>
  </>
  );
}