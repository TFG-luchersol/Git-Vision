import { stringToColor } from '@utils/tools';
import { ArcElement, Chart as ChartJS, Legend, Tooltip } from 'chart.js';
import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

export default function PieChart({numBytes=0, labels=[], data=[], size=300, showPercentaje}) {
    
    const [backgroundColor, setBackgroundColor] = useState([])

    useEffect(() => {
        const colors = labels.map(key => stringToColor(key));
        setBackgroundColor(colors);
    }, [labels, numBytes])
    
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
            return showPercentaje ?
                 `${tooltipItem.label}: ${(100 * tooltipItem.raw).toFixed(2) } %`:
                 `${tooltipItem.label}: ${(numBytes * tooltipItem.raw).toFixed(0)} bytes`;
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
