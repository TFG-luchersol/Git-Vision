import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

const DateRangePicker = ({ startDate, endDate, handleStartDateChange, handleEndDateChange }) => {

  return (
    <div style={{display:"flex", flexDirection:"column"}}>
        <div>
            <label>Fecha de inicio:</label>
            <DatePicker
                selected={startDate}
                onChange={handleStartDateChange}
                selectsStart
                startDate={startDate}
                endDate={endDate}
                dateFormat="dd/MM/yyyy"
                placeholderText="Selecciona una fecha"
            />
        </div>
        <div>
            <label>Fecha de fin:</label>
            <DatePicker
                selected={endDate}
                onChange={handleEndDateChange}
                selectsEnd
                startDate={startDate}
                endDate={endDate}
                minDate={startDate}  // Asegura que no se pueda seleccionar una fecha de fin anterior a la de inicio
                dateFormat="dd/MM/yyyy"
                placeholderText="Selecciona una fecha"
            />
        </div>
    </div>
  );
};

export default DateRangePicker;
