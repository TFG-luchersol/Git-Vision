import { useNotification } from "@context/NotificationContext";
import fetchWithToken from "@utils/fetchWithToken";
import getBody from "@utils/getBody";
import { BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip } from "chart.js";
import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const LinePerTimeInIssue = ({ parameter = "numCommits" }) => {
    const {showMessage} = useNotification();

    const [data, setData] = useState([]);
    const [chartData, setChartData] = useState(null);
    const [totalData, setTotalData] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetchWithToken("/api/contributors/stadistics");
                const data = await getBody(response);
                setData(data);
            } catch (error) {
                showMessage({
                    message: error.message
                })
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (data.length > 0) {
            const labels = data.map((entry) => entry.username);
            const values = data.map((entry) => entry[parameter].total / entry[parameter].time);
            const totalValues = data.map((entry) => entry[parameter].total);
            const totalTime = data.reduce((acc, entry) => acc + entry[parameter].time, 0);
            const totalSum = totalValues.reduce((acc, value) => acc + value, 0);

            setChartData({
                labels,
                datasets: [
                    {
                        label: `${parameter} per time`,
                        data: values,
                        backgroundColor: "rgba(75, 192, 192, 0.6)",
                        borderColor: "rgba(75, 192, 192, 1)",
                        borderWidth: 1,
                    },
                ],
            });

            setTotalData({
                totalSum,
                totalTime,
                percentage: ((totalSum / totalTime) || 0).toFixed(2),
            });
        }
    }, [data, parameter]);

    return (
        <div>
            <h2>Time Per Line in Issue</h2>
            {chartData && (
                <div>
                    <Bar
                        data={chartData}
                        options={{
                            responsive: true,
                            plugins: {
                                legend: { position: "top" },
                                title: { display: true, text: `${parameter} per time` },
                            },
                        }}
                    />
                </div>
            )}
            {totalData && (
                <div>
                    <h3>Total {parameter} Statistics</h3>
                    <p>Total {parameter}: {totalData.totalSum}</p>
                    <p>Total Time: {totalData.totalTime}</p>
                    <p>Average {parameter} per Time: {totalData.percentage}</p>
                </div>
            )}
        </div>
    );
};

export default LinePerTimeInIssue;
