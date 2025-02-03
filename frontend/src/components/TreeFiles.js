import React from 'react';
import { useParams } from 'react-router-dom';
import AccordionItem from './AccordionItem.js';

export default function TreeFiles({styleText={}, href=null, root, filter = "", deepFilter=false, filterExtension = [] }) {
    
    const {repo, owner} = useParams();

    const filterTree = (node) => {
        if (!filter && filterExtension.length === 0) return true;

        const nodeMatchesText = node.name.toLowerCase().includes(filter.toLowerCase());
        if (filterExtension.length === 0 ) {
            return nodeMatchesText;
        }

        if (node.leaf) {
            const nodeMatchesExtension = filterExtension.includes(node.extension);
            return nodeMatchesText && nodeMatchesExtension;
        }

        const childrenMatch = deepFilter ? node.children?.some(child => {
            console.log(child)
            return filterTree(child);
        }) : false;

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
                <AccordionItem 
                    styleText={styleText} 
                    href={window.location.origin + `/repository/${owner}/${repo}/blob/${removeCharsLeftOfFirstSlash(fullPath)}`} 
                    fullPath={fullPath} 
                    title={node.name} 
                    leaf 
                />
            );
        } else {
            const children = node.children
                ?.map(child => showTree(child, fullPath))
                .filter(child => child !== null);
            return (
                <AccordionItem 
                    styleText={styleText} 
                    fullPath={fullPath}
                    href={window.location.origin + `/repository/${owner}/${repo}/tree/${removeCharsLeftOfFirstSlash(fullPath)}`} 
                    title={node.name} 
                >
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
