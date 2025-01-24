import React, { useState } from 'react';

function AccordionItem({href=null, title, children, leaf }) {
    const [isOpen, setIsOpen] = useState(false);

    const toggleOpen = () => setIsOpen(!isOpen);

    return (
        <div style={{ marginLeft: '20px', marginTop: '10px' }}>
            <div onClick={toggleOpen} style={{ cursor: 'pointer', fontWeight: 'bold', display:"flex", flexDirection: "row" }}>
                {href ? 
                    <a style={{textDecoration: "none", color:"black"}} href={href}>{title}</a> : 
                    <p>{title}</p>} 
                <div style={{marginLeft: 10}}>{leaf ? children : isOpen ? '▼' : '►'}
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