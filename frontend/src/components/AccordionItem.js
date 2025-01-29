import React, { useState } from 'react';
import './accordionItem.css';
import { IoMdArrowDropdownCircle, IoMdArrowDroprightCircle } from "react-icons/io";

function AccordionItem({href=null, title, children, leaf }) {
    const [isOpen, setIsOpen] = useState(false);

    const toggleOpen = () => setIsOpen(!isOpen);
    const styleArrows = {fontSize: 18}
    return (
        <div style={{ marginLeft: '20px', marginTop: '10px' }}>
            <div onClick={toggleOpen} style={{ cursor: 'pointer', fontWeight: 'bold', display:"flex", flexDirection: "row" }}>
                {href ? 
                    <a className='text-hover' href={href}>{title}</a> : 
                    <p  className='text-hover'>{title}</p>} 
                <div style={{marginLeft: 10}}>
                    {leaf ? 
                        children : isOpen ? 
                            <IoMdArrowDropdownCircle style={styleArrows}/> : 
                            <IoMdArrowDroprightCircle style={styleArrows}/>
                    }
                </div>
            </div>
            {!leaf && isOpen && (
                <div >
                    {children}
                </div>
            )}
        </div>
    );
};

export default AccordionItem;