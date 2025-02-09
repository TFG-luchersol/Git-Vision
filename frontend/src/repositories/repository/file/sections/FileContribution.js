import React, { useState } from 'react';
import getBody from '../../../../util/getBody';

export default function FileContribution() {

    // {usuario: [{add, deletions}, ...], ...}
    const [contributions, setContributions] = useState([])

    async function FileContribution() {
        try {
            const response = await fetch("");
            const json = await response.json()
            const { xxx } = getBody(json);
        } catch (error) {
            alert(error)
        }
        
    }

    return (<div>


    </div>);

}