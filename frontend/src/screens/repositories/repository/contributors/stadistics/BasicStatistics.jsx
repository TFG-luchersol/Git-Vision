import DelayedButton from "@components/DelayedButton";
import { useNotification } from "@context/NotificationContext";
import fetchBackend from "@utils/fetchBackend";
import getBody from "@utils/getBody";
import {
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    Title,
    Tooltip
} from 'chart.js';
import React, { useState } from "react";
import { Bar } from 'react-chartjs-2';
import { useParams } from 'react-router-dom';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

export default function BasicStatistics() {
  const { owner, repo } = useParams();
  const { showMessage } = useNotification();
  const [isButtonDisabled, setIsButtonDisabled] = useState(false);
  const [stats, setStats] = useState(null);

  const getBasicStatistics = async () => {
    try {
        setIsButtonDisabled(true);
      const response = await fetchBackend(`/api/v1/contributions/${owner}/${repo}/basic_statistics`);
      const { commitCount, openIssues, closedIssues, openPRs, closedPRs } = await getBody(response);
      setStats({ commitCount, openIssues, closedIssues, openPRs, closedPRs });
    } catch (error) {
      showMessage({ message: error.message });
    } finally {
        setIsButtonDisabled(false);
    }
  };

  const getHorizontalBarOptions = (title) => ({
    indexAxis: 'y',
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      title: { display: true, text: title }
    },
    elements: {
      bar: {
        barThickness: 20 // Puedes bajar este valor a 10 o 15 si quieres aún más delgadas
      }
    },
    scales: {
      x: { beginAtZero: true },
      y: { ticks: { font: { size: 14 } } }
    }
  });

  const issuesData = {
    labels: ['Abiertas', 'Cerradas'],
    datasets: [{
      label: 'Issues',
      data: stats ? [stats.openIssues, stats.closedIssues] : [],
      backgroundColor: ['#f39c12', '#27ae60']
    }]
  };

  const prsData = {
    labels: ['Abiertas', 'Cerradas'],
    datasets: [{
      label: 'Pull Requests',
      data: stats ? [stats.openPRs, stats.closedPRs] : [],
      backgroundColor: ['#3498db', '#9b59b6']
    }]
  };

  return (
    <>
      <DelayedButton text={"Buscar"} onClick={getBasicStatistics} disabled={isButtonDisabled} />

      {stats && (
        <div style={{ marginTop: "20px" }}>
          <p><strong>Total Commits:</strong> {stats.commitCount}</p>

          <div style={{ marginTop: "30px", height: "150px" }}>
            <Bar data={issuesData} options={getHorizontalBarOptions("Issues")} />
          </div>

          <div style={{ marginTop: "30px", height: "150px" }}>
            <Bar data={prsData} options={getHorizontalBarOptions("Pull Requests")} />
          </div>
        </div>
      )}
    </>
  );
}
