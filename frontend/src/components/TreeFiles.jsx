import React from 'react';
import { useParams } from 'react-router-dom';
import AccordionItem from './AccordionItem';

export default function TreeFiles({styleText={}, href=null, root, filter = "", deepFilter=false, filterExtension = [] }) {
    
    const {repo, owner} = useParams();

    const filterTree = (node) => {
        // Si no hay ningún filtro, se acepta todo
        if (!filter && filterExtension.length === 0) return true;

        const nodeMatchesText = filter
            ? node.name.toLowerCase().includes(filter.toLowerCase())
            : true;

        const nodeMatchesExtension = node.leaf && filterExtension.length > 0
            ? filterExtension.includes(node.extension)
            : true;

        // Si el nodo es hoja, se evalúan directamente sus criterios
        if (node.leaf) {
            return nodeMatchesText && nodeMatchesExtension;
        }
        
        // Si no es hoja y deepFilter está activo, revisar hijos recursivamente
        if (!node.leaf && deepFilter) {
            return nodeMatchesText || node.children.some(child => filterTree(child));
        }
        // Si no es hoja, no hay deepFilter, y no cumple el nombre, no pasa
        return nodeMatchesText;
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
                ?.sort((a, b) => {
                    if (a.leaf === b.leaf) {
                        return a.name.localeCompare(b.name);
                    } else {
                        return !a.leaf ? -1 : 1;
                    }
                }).map(child => showTree(child, fullPath))
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
