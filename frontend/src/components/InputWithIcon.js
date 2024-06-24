import React from 'react';
import "./input.css";
import { Input, Label } from 'reactstrap';
export default function InputWithIcon({ icon, type, name, placeholder, value, onChange, label }) {
    return (
        <>
            <Label>{label}</Label>
            <div className="icon-input-container">
                
                <span className="icon">{icon}</span>
                <Input 
                type={type} 
                name={name}
                placeholder={placeholder} 
                value={value} 
                onChange={onChange} 
                />
            </div>
        </>
    );
  };