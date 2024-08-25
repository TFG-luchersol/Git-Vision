import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

export default function PieChart({labels=[], data=[], size=300}) {

    const [backgroundColor, setBackgroundColor] = useState([])
    const [borderColor, setBorderColor] = useState([])

    useEffect(() => {
        const colors = labels.map(key => stringToColor(key));
        const lineColor = colors.map(key => darkenHexColor(key));
        setBackgroundColor(colors);
        setBorderColor(lineColor);
    }, [labels])

    function stringToColor(str) {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            hash = str.charCodeAt(i) + ((hash << 5) - hash);
        }
        let color = '#'+((hash & 0x00FFFFFF).toString(16).padStart(6, '0')).toUpperCase();
        return color;
    }

    function darkenHexColor(hex, percent=0.1) {
        // Asegurarse de que el formato hexadecimal tenga el prefijo "#"
        hex = hex.replace(/^#/, '');
    
        // Convertir los valores de color a números enteros
        let r = parseInt(hex.substring(0, 2), 16);
        let g = parseInt(hex.substring(2, 4), 16);
        let b = parseInt(hex.substring(4, 6), 16);
    
        // Reducir el brillo del color
        r = Math.max(0, Math.min(255, r * (1 - percent / 100)));
        g = Math.max(0, Math.min(255, g * (1 - percent / 100)));
        b = Math.max(0, Math.min(255, b * (1 - percent / 100)));
    
        // Convertir los valores de color de vuelta a formato hexadecimal
        return `#${Math.round(r).toString(16).padStart(2, '0')}${Math.round(g).toString(16).padStart(2, '0')}${Math.round(b).toString(16).padStart(2, '0')}`;
    }
    
    const data_ = {
    labels,
    datasets: [
      {
        label: 'Languajes',
        data,
        backgroundColor,
        borderColor,
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'left',
      },
      tooltip: {
        callbacks: {
          label: function (tooltipItem) {
            return `${tooltipItem.label}: ${tooltipItem.raw}`;
          },
        },
      },
    },
  };

  return <div style={{height: size, width: size}}>
    <Pie data={data_} options={options} />
  </div>
  
  ;
};
