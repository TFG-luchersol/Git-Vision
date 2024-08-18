import React from 'react';
import AccordionItem from '../AccordionItem.js';

export default function TreeFiles({root}) {

    const showTree = (node) => {
        return node.leaf ? (<AccordionItem title={node.name} leaf />) :
            (<AccordionItem title={node.name}>
                {node.children?.map(child => showTree(child))}
            </AccordionItem>);
    }

    return (
        <div style={{marginLeft: '40px', marginTop: '20px'}}>
            {root.children?.map(child => showTree(child))}
        </div>
    );
}