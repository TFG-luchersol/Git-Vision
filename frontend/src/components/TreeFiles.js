import React from 'react';
import AccordionItem from './AccordionItem.js';

export default function TreeFiles({ root, filter = "", filterExtension = [] }) {
    
    const filterTree = (node) => {
        if (!filter && filterExtension.length === 0) return true;

        const nodeMatchesText = node.name.toLowerCase().includes(filter.toLowerCase());
        
        // En caso de no filtrar por extensión, mirar solo que cumpla con el texto
        if (filterExtension.length === 0){
            return nodeMatchesText
        }
        
        // En este punto, sabemos que se filtra por extensión, comprobamos filtrado por texto y extensión si es hoja
        if (node.leaf) {
            const nodeMatchesExtension = filterExtension.includes(node.extension);
            return nodeMatchesText && nodeMatchesExtension;
        }

        // Si no es hoja, profundizamos
        const childrenMatch = node.children?.some(child => filterTree(child));
        return childrenMatch;
    };

    const showTree = (node) => {
        const nodeMatches = filterTree(node);

        // Si el nodo no coincide con ningún filtro, se oculta
        if (!nodeMatches) {
            return null;
        }

        // Si es un archivo, se muestra directamente
        if (node.leaf) {
            return (
                <AccordionItem key={node.name} title={node.name} leaf />
            );
        } else {
            // Si es una carpeta, renderiza sus hijos
            const children = node.children
                ?.map(child => showTree(child))
                .filter(child => child !== null); // Filtramos nulos

            // Solo mostramos la carpeta si tiene hijos que coinciden
            return (
                <AccordionItem key={node.name} title={node.name}>
                    {children}
                </AccordionItem>
            );
        }
    };

    return (
        <div>
            {root.children?.sort((a, b) => {
                // Primero comparamos por tipo (carpeta o fichero)
                if (a.leaf === b.leaf) {
                    return a.name.localeCompare(b.name);
                } else {
                    // Asumimos que las carpetas van antes que los ficheros
                    return !a.leaf ? -1 : 1;
                }
            }).map(child => showTree(child))}
        </div>
    );
}
