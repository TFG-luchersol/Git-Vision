import React from 'react';
import { Input, Label } from 'reactstrap';
import '../static/css/components/input.css';

export default function CustomInput({ icon, type, readOnly, name, placeholder, value, onChange, label, button, min, max }) {
    return (
        <>
            <Label style={{ textAlign: 'left' }}>{label}</Label>
            <div className="icon-input-container">
                <span className="icon">{icon}</span>

                <Input
                    type={type}
                    name={name}
                    placeholder={placeholder}
                    value={value}
                    readOnly={readOnly}
                    onChange={onChange}
                    min={min}
                    max={max}
                />
                {button}


            </div>
        </>
    );
};