import React, { useEffect, useState } from "react";
import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css";

export default function SwaggerDocs(){
    const [docs,setDocs]=useState({});
    
    useEffect(() => {
        loadDocs();
    },[]);

    async function loadDocs() {
        try {
            const response = await fetch(`/v3/api-docs`, {
                headers: {
                    "Content-Type": "application/json",
                },
            });
            const mydocs = await response.json()
            setDocs(mydocs);
        } catch (error) {
            alert(error)
        }

    }    
    
    return (
        <SwaggerUI spec={docs} />
    );
    
}