import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

export default function PieChart({numFiles=0, labels=[], data=[], size=300, showPercentaje}) {

    const [backgroundColor, setBackgroundColor] = useState([])

    useEffect(() => {
        const colors = labels.map(key => stringToColor(key));
        setBackgroundColor(colors);
    }, [labels])

    function stringToColor(str) {
      let hash = 0;
      for (let i = 0; i < str.length; i++) {
          hash = str.charCodeAt(i) + ((hash << 5) - hash);
      }
  
      // Extraer los componentes RGB
      let r = (hash >> 16) & 0xFF;
      let g = (hash >> 8) & 0xFF;
      let b = hash & 0xFF;
  
      // Aumentar la saturación y la intensidad
      r = (r * 2) % 256; // Multiplica por 2
      g = (g * 2) % 256; // Multiplica por 2
      b = (b * 2) % 256; // Multiplica por 2
  
      // Asegurar que los valores sean altos
      r = Math.min(255, r + 50); // Aumentar un poco
      g = Math.min(255, g + 50);
      b = Math.min(255, b + 50);
  
      // Forzar un mínimo en los valores de color para mejorar la intensidad
      r = Math.max(128, r); // Mantener el mínimo en 128
      g = Math.max(128, g);
      b = Math.max(128, b);
  
      // Convertir a formato hexadecimal
      let color = '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1).toUpperCase();
  
      return color;
  }
    
    const data_ = {
    labels,
    datasets: [
      {
        label: 'Languajes',
        data,
        backgroundColor,
        borderColor: "#585863",
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
            console.log(numFiles)
            console.log(tooltipItem.raw)
            return showPercentaje ?
                 `${tooltipItem.label}: ${(100 * tooltipItem.raw).toFixed(2) } %`:
                 `${tooltipItem.label}: ${(numFiles * tooltipItem.raw).toFixed(0)} bytes`;
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
