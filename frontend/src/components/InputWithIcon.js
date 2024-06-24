import React from 'react';
import "./input.css";
import { Input, Label } from 'reactstrap';
export default function InputWithIcon({ icon, type, readOnly, name, placeholder, value, onChange, label, button }) {
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
                />
                {button}


            </div>
        </>
    );
};