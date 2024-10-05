import React from 'react';
import AccordionItem from './AccordionItem.js';

export default function TreeFiles({ root }) {

    const showTree = (node) => {
        return node.leaf ? 
            (<AccordionItem title={node.name} leaf />) :
            (<AccordionItem title={node.name}>
                {node.children?.sort((a, b) => {
                    // Primero comparamos por tipo (carpeta o fichero)
                    if (a.leaf === b.leaf) {
                        // Si ambos son del mismo tipo, ordenamos alfabéticamente por nombre
                        return a.name.localeCompare(b.name);
                    } else {
                        // Asumimos que las carpetas van antes que los ficheros
                        return !a.leaf || a.name.includes("/") ? -1 : 1;
                    }
                }).map(child => showTree(child))}
            </AccordionItem>)
    }

    return (
        <div>
            {root.children?.sort((a, b) => {
                    // Primero comparamos por tipo (carpeta o fichero)
                    if (a.leaf === b.leaf) {
                        // Si ambos son del mismo tipo, ordenamos alfabéticamente por nombre
                        return a.name.localeCompare(b.name);
                    } else {
                        // Asumimos que las carpetas van antes que los ficheros
                        return !a.leaf || a.name.includes("/") ? -1 : 1;
                    }
                }).map(child => showTree(child))}
        </div>
    );
}