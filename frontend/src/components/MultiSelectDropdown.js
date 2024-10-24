import React, { useState } from 'react';
import { FaFilter } from 'react-icons/fa';
import { Input } from 'reactstrap';

const MultiSelectDropdown = ({width, options, selectedOptions, setSelectedOptions}) => {

    const [isOpen, setIsOpen] = useState(false);
    const [isSelectAll, setIsSelectAll] = useState(false);

    const handleChange = (event) => {
        const value = event.target.value;

        setSelectedOptions((prev) => {
            if (prev.includes(value)) {
                setIsSelectAll(false);
                return prev.filter(option => option !== value);
            } else {
                return [...prev, value];
            }
        });
    };

    const handleSelectAll = () => {
        setSelectedOptions(isSelectAll ? [] : options);
        setIsSelectAll(prev => !prev);
    }

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div style={{width}}>
            <div onClick={toggleDropdown} style={{border: '1px solid #ccc', padding: '10px', borderTopRightRadius: 10, borderTopLeftRadius: 10}}>
                <FaFilter style={{fontSize: 20, marginInline: "10px", cursor: 'pointer', border: "1px solid #bbb", padding: 3, borderRadius: 2, boxShadow: 10 }} />
                Filter Extensions: {selectedOptions.length > 0 && `(${selectedOptions.length})`}
            </div>
            {isOpen && (
                <div style={{width: "30%", maxHeight: "35%",  overflow: "auto", border: '1px solid #ccc', padding: '10px', position: 'absolute', background: 'white' }}>
                    {options.map(option => (
                        <div key={option}>
                            {option === "Unknown" && <hr/>}
                            <label>
                                <Input
                                    style={{transition: "background-color 0.3s ease, transform 0.3s ease"}}
                                    type="checkbox"
                                    value={option}
                                    checked={selectedOptions.includes(option)}
                                    onChange={handleChange}
                                />
                                &nbsp;&nbsp;&nbsp;{option}
                            </label>
                        </div>
                    ))}
                    <div key="select-all">
                        <label>
                            <Input
                                style={{transition: "background-color 0.3s ease, transform 0.3s ease"}}
                                type="checkbox"
                                value={isSelectAll}
                                checked={isSelectAll}
                                onChange={handleSelectAll}
                            />
                            &nbsp;&nbsp;&nbsp;Select all
                        </label>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MultiSelectDropdown;
