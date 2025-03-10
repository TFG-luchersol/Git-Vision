import React, { useState } from "react";
import { IoCloseOutline } from "react-icons/io5";
import { Alert } from "reactstrap";


export default function AlertMessage({message}){
    const [isOpen, setIsOpen] = useState(Boolean(message))

    return <Alert isOpen={isOpen} color="danger" style={{ zIndex: 2, position: 'fixed', top: '15%' }}>
                <IoCloseOutline onClick={()=>setIsOpen(prev=>!prev)} style={{position: "absolute", right:0, top:0}}/>
                {message}
            </Alert>
}