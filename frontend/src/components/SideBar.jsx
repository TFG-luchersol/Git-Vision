import "@css/components/sideBar";
import React from 'react';
import { SlArrowDown, SlArrowRight } from "react-icons/sl";
import ReactMarkdown from 'react-markdown';
import { Button } from 'reactstrap';
import remarkGfm from 'remark-gfm';
import remarkSlug from 'remark-slug';

export default function SideBar ({ isOpen, toggleSidebar, indexMarkdown }){
    return (<div className={`sidebar ${isOpen ? 'open' : ''}`}>
        <Button onClick={toggleSidebar} className="w-full mb-4">
            {isOpen ? <SlArrowRight/ > : <SlArrowDown/ >}
        </Button>
        {isOpen && <div className="index-container">
            <ReactMarkdown
                children={indexMarkdown}
                remarkPlugins={[remarkSlug, remarkGfm]}
            />
        </div>}
    </div>);
};
