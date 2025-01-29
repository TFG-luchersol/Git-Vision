import React, { useState, useEffect } from 'react';
import getBody from '../../../../util/getBody';
import { useParams } from 'react-router-dom';
import tokenService from '../../../../services/token.service';
import hljs from "highlight.js";
import "highlight.js/styles/default.css"; // Cambia el estilo según prefieras
import { marked } from "marked";

export default function FileContent() {
    const { owner, repo, "*": path } = useParams();
    const { username } = tokenService.getUser();

    const [contentFile, setContentFile] = useState("");
    const [language, setLanguage] = useState("plaintext"); // Idioma predeterminado

    useEffect(() => {
        getContent();
    }, []);

    useEffect(() => {
        hljs.highlightAll(); // Aplica el resaltado después de cada render
    }, [contentFile]);

    function isMarkdownFile(fileName) {
        const markdownExtensions = ['.md', '.markdown', '.mdown'];
        return markdownExtensions.some(extension => fileName.toLowerCase().endsWith(extension));
    }

    async function getContent() {
        try {
            const response = await fetch(`/api/v1/files/repository/${owner}/${repo}/blob/content?login=${username}&path=${path}`);
            const json = await response.json();
            const { content } = getBody(json);
            setContentFile(content);
            if(isMarkdownFile(path)) {
                setLanguage("markdown")
            } else {
                const detectedLanguage = hljs.highlightAuto(content).language;
                setLanguage(detectedLanguage || "plaintext"); // Fallback a plaintext
            }

        } catch (error) {
            alert(error.message);
        }
    }


    return (
        language === "markdown" ? 
            <div dangerouslySetInnerHTML={{ __html: marked(contentFile) }} /> :
            <pre style={{borderRadius: 10, height: "63vh"}}>
                <code className={`language-${language}`} style={{height: "100%"}}>{contentFile}</code>
            </pre>
    );
}
