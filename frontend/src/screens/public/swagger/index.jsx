import { useNotification } from '@context/NotificationContext';
import getBody from "@utils/getBody";
import React, { useEffect, useState } from "react";
import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css";

export default function SwaggerDocs(){
    const {showMessage} = useNotification();
    const [docs, setDocs] = useState(null);

    useEffect(() => {
        loadDocs();
    }, []);

    async function loadDocs() {
        try {
            const response = await fetch('http://localhost:8080/v3/api-docs');
            const mydocs = await getBody(response);
            setDocs(mydocs);
        } catch (error) {
            showMessage({
                message: error.message
            })
        }
    }    
    
    return (
        <SwaggerUI spec={docs} />
    );
    
}
