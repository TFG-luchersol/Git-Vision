import SideBar from "@components/SideBar";
import "@css/docs";
import React, { useEffect, useState } from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import remarkSlug from 'remark-slug';

export default function Documentation() {
    const [markdown, setMarkdown] = useState("");
    const [indexMarkdown, setIndexMarkdown] = useState("");
    const [isSidebarOpen, setIsSidebarOpen] = useState(true); // Para controlar el estado del sidebar

    function generarIndice(markdownText) {
        const encabezados = markdownText.match(/^(#{1,6})\s+(.*)/gm);
        
        if (!encabezados) return '';
    
        let indice = [];

        function generarId(texto) {
            return texto
                .toLowerCase()
                .replace(/[^a-z0-9áéíóúüñ\s-]/g, '')  // Permitir tildes, ñ y caracteres especiales
                .replace(/\s+/g, '-')                 // Reemplazar espacios por guiones
                .replace(/^-+|-+$/g, '');             // Eliminar guiones al principio o final
        }
    
        encabezados.forEach(encabezado => {
            const match = encabezado.match(/^(#{1,6})\s+(.*)/);
            if (match) {
                const nivel = match[1].length;
                const texto = match[2].trim();
                const id = generarId(texto);
                indice.push({ nivel, texto, id });
            }
        });
    
        let indiceFormateado = indice.map(({ nivel, texto, id }) => {
            const indentacion = '  '.repeat(nivel - 1);
            return `${indentacion}- [${texto}](#${encodeURIComponent(id)})`;
        });
    
        return indiceFormateado.join("\n");
    }

    useEffect(() => {
        fetch("/docs/index.md")
            .then(response => response.text())
            .then(content => {
                setMarkdown(content)
                console.log(generarIndice(content))
                setIndexMarkdown(generarIndice(content))
            })
            .catch(error => alert(error));
    }, []);
    
    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    return (
        <div className="items-column">
            <SideBar indexMarkdown={indexMarkdown} isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
            <div className={`content-docs ${isSidebarOpen ? 'content-docs-open' : ''}`}>
                <ReactMarkdown
                    children={markdown}
                    remarkPlugins={[remarkSlug, remarkGfm]}
                />
            </div>
        </div>

    );
}
