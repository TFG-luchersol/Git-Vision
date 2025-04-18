export default async function getBody(response: Response) {
    
    const json: object | string = await response.text().then(t => { 
        try { 
            return JSON.parse(t); 
        } catch { 
            return t; 
        } 
    }).catch(() => `ERROR NO PROCESADO ${response.status}`);
    

    /**
     * 200 -> OK
     * 400 -> BAD_REQUEST
     * 404 -> NOT_FOUND
     * 500 -> INTERNAL_SERVER_ERROR
     */
    const handlers: Record<number, Function> = {
        200: () => json,
        400: () => { 
            let errorMessage = 'Respuesta en formato diferente a Object o String';
            if(json && typeof json === 'object') {
                errorMessage = Object.entries(json)
                .map(([field, message]) => `• ${message}`)
                .join('\n');
            }
            if(typeof json === 'string') {
                errorMessage = json;
            }
            throw new Error(errorMessage); 
        },
        404: () => { throw new Error(json.toString()); },
        500: () => { throw new Error(json.toString()); },
    };

    const handler = handlers[response.status];

    if (!handler) {
        throw new Error(`Estado ${response.status} no esperado`);
    }

    return handler();
}
