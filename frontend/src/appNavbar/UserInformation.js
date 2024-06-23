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
    const user = tokenService.getUser();
    if (user !== null) {
      tokenService.removeUser();
      console.log(tokenService.getUser());
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }
  

  let logo = user ? <img
    className='circular-img'
    src={'https://avatars.githubusercontent.com/u/93008812?v=4'}
    alt={<IoPersonCircleOutline id='avatarPopover' title='Avatar Error' style={{ color:'red', fontSize: 60 }} />}
    id='avatarPopover' 
  /> : <IoPersonCircleOutline id='avatarPopover' style={{ fontSize: 60, position: 'relative', bottom: 5 }} />;

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
          <Link to={'/register'}>Añadir cuenta</Link>
          <hr/>
          <Link onClick={sendLogoutRequest} >Cerrar sesión</Link>
        </div>
      </PopoverBody>
    </Popover>
  </>
  );
}