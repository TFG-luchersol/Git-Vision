import React, { useState } from 'react';

function AccordionItem({ title, children, leaf }) {
    const [isOpen, setIsOpen] = useState(false);

    const toggleOpen = () => setIsOpen(!isOpen);

    return (
        <div style={{ marginLeft: '20px', marginTop: '10px' }}>
            <div onClick={toggleOpen} style={{ cursor: 'pointer', fontWeight: 'bold' }}>
                {title} {leaf ? children : isOpen ? '▼' : '►'}
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