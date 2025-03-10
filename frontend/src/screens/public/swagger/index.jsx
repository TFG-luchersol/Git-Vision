import React, { useEffect, useState } from "react";
import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css";

export default function SwaggerDocs(){
    const [docs, setDocs] = useState(null);

    useEffect(() => {
        loadDocs();
    }, []);

    async function loadDocs() {
        try {
            const response = await fetch('http://localhost:8080/v3/api-docs');
            const mydocs = await response.json();
            setDocs(mydocs);
        } catch (error) {
            console.error(error);
        }
    }    
    
    return (
        <SwaggerUI spec={docs} />
    );
    
}