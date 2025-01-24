import React from 'react';
import AccordionItem from './AccordionItem.js';

export default function TreeFiles({href=null, root, filter = "", filterExtension = [] }) {
    
    const filterTree = (node) => {
        if (!filter && filterExtension.length === 0) return true;

        const nodeMatchesText = node.name.toLowerCase().includes(filter.toLowerCase());
        
        if (filterExtension.length === 0) {
            return nodeMatchesText;
        }
        
        if (node.leaf) {
            const nodeMatchesExtension = filterExtension.includes(node.extension);
            return nodeMatchesText && nodeMatchesExtension;
        }

        const childrenMatch = node.children?.some(child => filterTree(child));
        return childrenMatch;
    };

    function removeCharsLeftOfFirstSlash(inputString) {
        let index = -1
        for (let i = 0; i < inputString.length; i++) {
            if(inputString[i] !== "/") {
                index = i;
                break;
            }
            
        }
        return index !== -1 ? inputString.slice(index) : inputString;
    }

    const showTree = (node, currentPath = "") => {
        const nodeMatches = filterTree(node);

        if (!nodeMatches) {
            return null;
        }

        const fullPath = `${currentPath}/${node.name}`;

        if (node.leaf) {
            return (
                <AccordionItem href={`${window.location.href}/blob/${removeCharsLeftOfFirstSlash(fullPath)}`} fullPath={fullPath} title={node.name} leaf />
            );
        } else {
            const children = node.children
                ?.map(child => showTree(child, fullPath))
                .filter(child => child !== null);
            return (
                <AccordionItem href={`${window.location.href}/tree/${removeCharsLeftOfFirstSlash(fullPath)}`} title={node.name} >
                    {children}
                </AccordionItem>
            );
        }
    };

    return (
        <div>
            {root.children?.sort((a, b) => {
                if (a.leaf === b.leaf) {
                    return a.name.localeCompare(b.name);
                } else {
                    return !a.leaf ? -1 : 1;
                }
            }).map(child => showTree(child, root.name))}
        </div>
    );
}
