import React from 'react';
import "../static/css/appnavbar/appnavbar.css";
import { Popover, PopoverBody } from 'reactstrap';
import tokenService from '../services/token.service';
import { Link } from 'react-router-dom';
import { IoPersonCircleOutline } from "react-icons/io5";

export default function UserInformation({placement, isOpen, target, toggle}) {

  const [popoverOpen, setPopoverOpen] = React.useState(false);
  const togglePopover = () => setPopoverOpen(!popoverOpen);
  
  function sendLogoutRequest() {
    const jwt = window.localStorage.getItem("jwt");
    if (jwt || typeof jwt === "undefined") {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }

  let logo = true ?
    <IoPersonCircleOutline 
      style={{fontSize:60}} 
      alt='User Avatar' 
      id="avatarPopover" 
      /> :
    <img 
      src='https://avatars.githubusercontent.com/u/583231?v=4'
      style={{scale:40}} 
      alt='User Avatar'  
      id='avatarPopover' />;

  return (<>
    {logo}
    {/* <Popover
      placement="bottom"
      isOpen={popoverOpen}
      target="avatarPopover"
      toggle={togglePopover}
    >
      <PopoverBody>
        <div className='information'>
          <Link to={'/details'} >Información de usuario</Link>
          <Link to={'/registrer'}>Añadir cuenta</Link>
          <hr></hr>
          <Link className='custom-link' onClick={sendLogoutRequest} >Cerrar sesión</Link>
        </div>
      </PopoverBody>
    </Popover> */}
  </>
  );
}