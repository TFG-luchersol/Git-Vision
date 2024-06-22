import React, {useState} from 'react';
import { Alert, Button, Form, FormGroup, Input, Label } from 'reactstrap';

export default function InputWithIcon({icon, input}) {

    return (
        <div>
            <div>{icon}</div>
            {input}
        </div>
    );
}