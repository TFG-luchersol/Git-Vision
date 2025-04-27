import DelayedButton from '@components/DelayedButton';
import { useNotification } from '@context/NotificationContext';
import {
    Box,
    MenuItem,
    Select,
    TextField,
    Typography
} from "@mui/material";
import tokenService from '@services/token.service';
import fetchBackend from "@utils/fetchBackend";
import getBody from '@utils/getBody';
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
    const { showMessage } = useNotification();

    const [queryParamType, setQueryParamType] = useState("name");
    const [queryParamValue, setQueryParamValue] = useState("");
    const [taskName, setTaskName] = useState("");
    const [dataType, setDataType] = useState("additions");
    const [timeUnit, setTimeUnit] = useState("hours");
    const [showByTime, setShowByTime] = useState(false);
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);

    const [rawData, setRawData] = useState(null);
    const [chartData, setChartData] = useState(null);
    const [summary, setSummary] = useState(null);

    function getSingularTimeUnit() {
        if(timeUnit === "hours")
            return "hour";
        if(timeUnit === "minutes")
            return "minute";
    }

    const fetchData = async () => {
        const queryParams = {[queryParamType]: queryParamValue};
        if (taskName)
            queryParams["taskName"] = taskName;

        try {
            setIsButtonDisabled(prev => true);
            const response = await fetchBackend(
                `/api/v1/contributions/${owner}/${repo}/issue`, {}, queryParams
            );
            const result = await getBody(response);
            setRawData(result);
        } catch (error) {
            showMessage({
                message: error.message
            });
        } finally {
            setIsButtonDisabled(prev => false);
        }
    };

    const colorsGraph = {
        additions: {
            backgroundColor: "rgba(76, 175, 80, 0.6)", // Verde claro
            borderColor: "rgba(76, 175, 80, 1)" // Verde fuerte
        },
        deletions: {
            backgroundColor: "rgba(255, 99, 132, 0.6)", // Rojo claro
            borderColor: "rgba(255, 99, 132, 1)" // Rojo fuerte
        },
        changes: {
            backgroundColor: "rgba(255, 165, 0, 0.6)", // Naranja claro
            borderColor: "rgba(255, 165, 0, 1)" // Naranja fuerte
        },
    }

    useEffect(() => {
        if (!rawData) return;

        const usernames = Object.keys(rawData);

        // Calcular el tiempo y los valores según la opción seleccionada
        const times = usernames.map((username) => {
            const baseTime = rawData[username].time / 3_600_000_000_000.0;
            return timeUnit === "minutes" ? baseTime * 60 : baseTime;
        });

        const values = usernames.map((username) =>
            dataType === "changes"
                ? rawData[username].additions + rawData[username].deletions
                : rawData[username][dataType]
        );

        const totalTime = times.reduce((acc, time) => acc + time, 0);
        const totalValues = values.reduce((acc, value) => acc + value, 0);

        // Configurar los datos de la gráfica según si estamos mostrando por tiempo o por total
        setChartData({
            labels: usernames,
            datasets: [
                {
                    label: `${dataType} ${showByTime ? `/ ${getSingularTimeUnit()}` : ""}`,
                    data: values.map((value, index) =>
                        showByTime && times[index] === 0 ? 0 : showByTime ? value / times[index] : value
                    ),
                    borderWidth: 1,
                    maxBarThickness: 40,
                    ...colorsGraph[dataType],
                },
            ],
        });

        setSummary({
            totalTime,
            totalValues,
        });
    }, [rawData, dataType, timeUnit, showByTime]); // Añadir showByTime a las dependencias

    return (
        <Box sx={{ padding: 2 }}>
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

                {tokenService.hasClockifyToken() && 
                <TextField
                    size="small"
                    label="Task Name (Optional)"
                    value={taskName}
                    onChange={(e) => setTaskName(e.target.value)}
                />}
                

                <Select
                    value={dataType}
                    onChange={(e) => setDataType(e.target.value)}
                    size="small"
                >
                    <MenuItem value="additions">Additions</MenuItem>
                    <MenuItem value="deletions">Deletions</MenuItem>
                    <MenuItem value="changes">Changes</MenuItem>
                </Select>

                {tokenService.hasClockifyToken() && <>
                <Select
                    value={timeUnit}
                    onChange={(e) => setTimeUnit(e.target.value)}
                    size="small"
                >
                    <MenuItem value="hours">Horas</MenuItem>
                    <MenuItem value="minutes">Minutos</MenuItem>
                </Select>

                
                <Select
                    value={showByTime ? "time" : "total"}
                    onChange={(e) => setShowByTime(e.target.value === "time")}
                    size="small"
                >
                    <MenuItem value="time">Por Tiempo</MenuItem>
                    <MenuItem value="total">Total</MenuItem>
                </Select>
                </>}

                <DelayedButton text={"Buscar"} onClick={fetchData} disabled={isButtonDisabled}/>

                {summary && (
                    <div style={{borderRadius: "10px", 
                              border: "1px solid black", 
                              padding: "3px", 
                              marginLeft: "5px",
                              fontSize: 10}}>
                        {summary.totalTime > 0 ? <> 
                            <Typography variant="body2">
                                {summary.totalValues} {dataType} / {summary.totalTime.toFixed(2)} {timeUnit}
                            </Typography>
                            <Typography variant="body2">
                                {(summary.totalValues/summary.totalTime).toFixed(2)} {dataType}/{timeUnit}
                            </Typography>
                        </> : <Typography variant="body2">
                            {summary.totalValues} {dataType}
                        </Typography>}
                    </div>
                )}
            </Box>

            {chartData && (
                <Box sx={{ height: "55vh", marginBottom: 2 }}>
                    <Bar
                        data={chartData}
                        options={{
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    position: "top",
                                },
                            },
                        }}
                    />
                </Box>
            )}
        </Box>
    );
};

export default LinePerTimeInIssue;

