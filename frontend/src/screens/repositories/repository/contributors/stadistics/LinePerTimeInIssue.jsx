import {
    Box,
    Button,
    MenuItem,
    Select,
    TextField,
    Typography,
} from "@mui/material";
import fetchBackend from "@utils/fetchBackend";
import {
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    Title,
    Tooltip,
} from "chart.js";
import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import { useParams } from "react-router-dom";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const LinePerTimeInIssue = () => {
    const { owner, repo } = useParams();
    const [queryParamType, setQueryParamType] = useState("name");
    const [queryParamValue, setQueryParamValue] = useState("");
    const [taskName, setTaskName] = useState("");
    const [dataType, setDataType] = useState("additions");
    const [timeUnit, setTimeUnit] = useState("hours");

    const [rawData, setRawData] = useState(null);
    const [chartData, setChartData] = useState(null);
    const [summary, setSummary] = useState(null);

    const fetchData = async () => {
        const queryParams = new URLSearchParams();
        queryParams.append(queryParamType, queryParamValue);
        if (taskName) queryParams.append("taskName", taskName);

        try {
            const response = await fetchBackend(
                `/api/v1/contributions/${owner}/${repo}/time?${queryParams.toString()}`
            );
            const result = await response.json();
            setRawData(result);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    useEffect(() => {
        if (!rawData) return;

        const usernames = Object.keys(rawData);

        const times = usernames.map((username) => {
            const baseTime = rawData[username].time / 3_600_000_000_000.0;
            return timeUnit === "minutes" ? baseTime * 60 : baseTime;
        });

        const values = usernames.map((username) =>
            dataType === "total"
                ? rawData[username].additions + rawData[username].deletions
                : rawData[username][dataType]
        );

        const totalTime = times.reduce((acc, time) => acc + time, 0);
        const totalValues = values.reduce((acc, value) => acc + value, 0);

        setChartData({
            labels: usernames,
            datasets: [
                {
                    label: `${dataType} / ${timeUnit}`,
                    data: values.map((value, index) =>
                        times[index] === 0 ? 0 : value / times[index]
                    ),
                    backgroundColor: "rgba(75, 192, 192, 0.6)",
                    borderColor: "rgba(75, 192, 192, 1)",
                    borderWidth: 1,
                },
            ],
        });

        setSummary({
            totalTime,
            totalValues,
        });
    }, [rawData, dataType, timeUnit]);

    return (
        <Box sx={{ padding: 2 }}>
            <Typography variant="h5" gutterBottom>
                Lines / Time Statistics
            </Typography>

            <Box sx={{ display: "flex", gap: 2, marginBottom: 2, flexWrap: "wrap" }}>
                <Select
                    value={queryParamType}
                    onChange={(e) => setQueryParamType(e.target.value)}
                    size="small"
                >
                    <MenuItem value="name">Name</MenuItem>
                    <MenuItem value="issueNumber">Issue Number</MenuItem>
                </Select>

                <TextField
                    size="small"
                    label="Value"
                    value={queryParamValue}
                    onChange={(e) => setQueryParamValue(e.target.value)}
                />

                <TextField
                    size="small"
                    label="Task Name (Optional)"
                    value={taskName}
                    onChange={(e) => setTaskName(e.target.value)}
                />

                <Select
                    value={dataType}
                    onChange={(e) => setDataType(e.target.value)}
                    size="small"
                >
                    <MenuItem value="additions">Additions</MenuItem>
                    <MenuItem value="deletions">Deletions</MenuItem>
                    <MenuItem value="total">Total</MenuItem>
                </Select>

                {/* 👉 Nuevo selector de unidad de tiempo */}
                <Select
                    value={timeUnit}
                    onChange={(e) => setTimeUnit(e.target.value)}
                    size="small"
                >
                    <MenuItem value="hours">Horas</MenuItem>
                    <MenuItem value="minutes">Minutos</MenuItem>
                </Select>

                <Button variant="contained" onClick={fetchData}>
                    Fetch Data
                </Button>
            </Box>

            {chartData && (
                <Box sx={{ height: "60vh", marginBottom: 2 }}>
                    <Bar
                        data={chartData}
                        options={{
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    position: "top",
                                },
                                title: {
                                    display: true,
                                    text: "Lines / Time Chart",
                                },
                            },
                        }}
                    />
                </Box>
            )}

            {summary && (
                <Box>
                    <Typography variant="body1">
                        Total Time: {summary.totalTime.toFixed(2)} {timeUnit}
                    </Typography>
                    <Typography variant="body1">
                        Total {dataType}: {summary.totalValues}
                    </Typography>
                </Box>
            )}
        </Box>
    );
};

export default LinePerTimeInIssue;
