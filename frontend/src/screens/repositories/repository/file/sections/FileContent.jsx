import fetchWithToken from '@utils/fetchWithToken.ts';
import getBody from '@utils/getBody.ts';
import hljs from "highlight.js";
import "highlight.js/styles/default.css";
import { marked } from "marked";
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

export default function FileContent() {
    const { owner, repo, "*": path } = useParams();

    const [contentFile, setContentFile] = useState("");
    const [language, setLanguage] = useState("plaintext");

    useEffect(() => {
        getContent();
    }, []);

    function base64ToString(base64) {
        const binaryString = atob(base64);
        
        const decoder = new TextDecoder('utf-8');
        const uint8Array = new Uint8Array(binaryString.length);
        
        for (let i = 0; i < binaryString.length; i++) {
          uint8Array[i] = binaryString.charCodeAt(i);
        }
      
        return decoder.decode(uint8Array);
      }
    function displayImageFromBytes(byteArray) {
        return `data:image/png;base64,${byteArray}`;
    }
    useEffect(() => {
        hljs.highlightAll();
    }, [contentFile]);

    function isMarkdownFile(fileName) {
        const markdownExtensions = ['.md', '.markdown', '.mdown'];
        return markdownExtensions.some(extension => fileName.toLowerCase().endsWith(extension));
    }

    function isImage(filePath) {
        const imageExtensions = /\.(jpg|jpeg|png|gif|bmp|webp|ico|tiff)$/i;
        return imageExtensions.test(filePath);
    }

    async function getContent() {
        try {
            const response = await fetchWithToken(`/api/v1/files/repository/${owner}/${repo}/blob/content?path=${path}`);
            const { content } = await getBody(response);
            if(isImage(path)){
                setContentFile(prev => displayImageFromBytes(content));
                setLanguage("image");
            } else {
                setContentFile(prev => base64ToString(content));
               
                if (isMarkdownFile(path)) {
                    setLanguage("markdown")
                } else {
                    const detectedLanguage = hljs.highlightAuto(content).language;
                    setLanguage(detectedLanguage || "plaintext"); // Fallback a plaintext
                }
            }

        } catch (error) {
            alert(error.message);
        }
    }


    return (
        language === "image" ?
        <img src={contentFile} alt="Imagen desde GitHub" /> :
        language === "markdown" ?
            <div dangerouslySetInnerHTML={{ __html: marked(contentFile) }} /> :
            <pre style={{ borderRadius: 10, height: "63vh" }}>
                <code className={`language-${language}`} style={{ height: "100%" }}>{contentFile}</code>
            </pre>
    );
}
