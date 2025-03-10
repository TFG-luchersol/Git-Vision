import React from 'react';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

const DateRangePicker = ({startDate, endDate, setStartDate, setEndDate}) => {

    const handleStartDateChange = (date) => {
        setStartDate(date);
        if (date && endDate && date > endDate) {
            setEndDate(null); // Reinicia la fecha de fin si la fecha de inicio es posterior
        }
    };

    const handleEndDateChange = (date) => {
        setEndDate(date);
    };

    return (
        <div style={{ display: "flex", flexDirection: "row" }}>
                <DatePicker
                    selected={startDate}
                    onChange={handleStartDateChange}
                    selectsStart
                    startDate={startDate}
                    endDate={endDate}
                    dateFormat="dd/MM/yyyy"
                    placeholderText="Selecciona una fecha"
                />
                <hr style={{position: "relative", bottom: 2, width: 50, height: 5, border: "none", backgroundColor: "rgb(0,0,0)", marginInline: 30}}></hr>
                <DatePicker
                    selected={endDate}
                    onChange={handleEndDateChange}
                    selectsEnd
                    startDate={startDate}
                    endDate={endDate}
                    minDate={startDate} // Asegura que la fecha de fin sea posterior o igual a la de inicio
                    dateFormat="dd/MM/yyyy"
                    placeholderText="Selecciona una fecha"
                />
        </div>
    );
};

export default DateRangePicker;
