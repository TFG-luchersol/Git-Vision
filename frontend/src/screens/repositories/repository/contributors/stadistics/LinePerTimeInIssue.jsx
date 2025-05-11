import DelayedButton from '@components/DelayedButton';
import { useNotification } from '@context/NotificationContext';
import {
    Box,
    Checkbox,
    MenuItem,
    Select,
    TextField,
    Typography
} from "@mui/material";
import tokenService from '@services/token.service';
import fetchBackend from "@utils/fetchBackend";
import getBody from '@utils/getBody';
import {
    ArcElement,
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    Title,
    Tooltip,
} from "chart.js";
import React, { useEffect, useState } from "react";
import { Pie } from "react-chartjs-2";
import { useParams } from "react-router-dom";
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

const LinePerTimeInIssue = () => {
    const { owner, repo } = useParams();
    const { showMessage } = useNotification();

    const [queryParamType, setQueryParamType] = useState("name");
    const [queryParamValue, setQueryParamValue] = useState("");
    const [taskName, setTaskName] = useState("");
    const [dataType, setDataType] = useState("additions");
    const [timeUnit, setTimeUnit] = useState("hours");
    const [showByTime, setShowByTime] = useState(false);
    const [inverseRatio, setInverseRatio] = useState(false); // Para invertir la relación
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);

    const [rawData, setRawData] = useState(null);
    const [chartData, setChartData] = useState(null);
    const [summary, setSummary] = useState(null);

    function getSingularTimeUnit() {
        if (timeUnit === "hours") return "hour";
        if (timeUnit === "minutes") return "minute";
    }

    const fetchData = async () => {
        const queryParams = { [queryParamType]: queryParamValue };
        if (taskName) queryParams["taskName"] = taskName;

        try {
            setIsButtonDisabled(true);
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
            setIsButtonDisabled(false);
        }
    };

    const colorsDataType = {
        additions: {
            backgroundColor: "rgba(76, 175, 80, 0.6)", 
            color: "white"
        },
        deletions: {
            backgroundColor: "rgba(255, 99, 132, 0.6)", 
            color: "white"
        },
        changes: {
            backgroundColor: "rgba(255, 165, 0, 0.6)",
            color: "white"
        },
    };

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
                    data: values.map((value, index) => {
                        if (!showByTime) return value;

                        const time = times[index];
                        if (inverseRatio) {
                            return value === 0 ? 0 : time / value;
                        } else {
                            return time === 0 ? 0 : value / time;
                        }
                    }),
                    backgroundColor: usernames.map((_, i) =>
                        `hsl(${(i * 360) / usernames.length}, 70%, 60%)`
                    ),
                    borderWidth: 1,
                    maxBarThickness: 40,
                },
            ],
        });

        setSummary({
            totalTime,
            totalValues,
        });
    }, [rawData, dataType, timeUnit, showByTime, inverseRatio]);

    return (
        <Box sx={{ padding: 2 }}>
            <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
                {/* Panel lateral de controles */}
                <Box sx={{ display: "flex", flexDirection: "column", gap: 2, width: 300 }}>
                    <Select
                        value={queryParamType}
                        onChange={(e) => setQueryParamType(e.target.value)}
                        size="small"
                        fullWidth
                    >
                        <MenuItem value="name">Nombre</MenuItem>
                        <MenuItem value="issueNumber">Número</MenuItem>
                    </Select>

                    <TextField
                        size="small"
                        label="Valor"
                        value={queryParamValue}
                        onChange={(e) => setQueryParamValue(e.target.value)}
                        fullWidth
                    />

                    {tokenService.hasClockifyToken() && (
                        <TextField
                            size="small"
                            label="Nombre Task (Opcional)"
                            value={taskName}
                            onChange={(e) => setTaskName(e.target.value)}
                            fullWidth
                        />
                    )}

                    <Select
                        value={dataType}
                        onChange={(e) => setDataType(e.target.value)}
                        size="small"
                        fullWidth
                        style={colorsDataType[dataType]}
                    >
                        <MenuItem value="additions" style={colorsDataType.additions}>Adiciones</MenuItem>
                        <MenuItem value="deletions" style={colorsDataType.deletions}>Borrados</MenuItem>
                        <MenuItem value="changes" style={colorsDataType.changes}>Cambios</MenuItem>
                    </Select>

                    {tokenService.hasClockifyToken() && (
                        <>
                            <Select
                                value={timeUnit}
                                onChange={(e) => setTimeUnit(e.target.value)}
                                size="small"
                                fullWidth
                            >
                                <MenuItem value="hours">Horas</MenuItem>
                                <MenuItem value="minutes">Minutos</MenuItem>
                            </Select>

                            <Select
                                value={showByTime ? "time" : "total"}
                                onChange={(e) => setShowByTime(e.target.value === "time")}
                                size="small"
                                fullWidth
                            >
                                <MenuItem value="time">Por Tiempo</MenuItem>
                                <MenuItem value="total">Total</MenuItem>
                            </Select>
                            <Box sx={{ display: "flex", alignItems: "center" }}>
                                <Checkbox
                                    checked={inverseRatio}
                                    onChange={(e) => setInverseRatio(e.target.checked)}
                                    size="small"
                                />
                                <Typography variant="body2">Invertir razón (tiempo / línea)</Typography>
                            </Box>
                        </>
                    )}

                    <DelayedButton text={"Buscar"} onClick={fetchData} disabled={isButtonDisabled} />
                </Box>

                {summary && (
                    <Box sx={{
                        borderRadius: "10px",
                        border: "1px solid black",
                        padding: "8px",
                        fontSize: 12,
                        minWidth: 200,
                        alignSelf: "flex-start",
                    }}>
                        {showByTime && summary.totalTime > 0 ? (
                            <>
                                {/* Mostrar línea por tiempo */}
                                {inverseRatio ? (
                                    <>
                                        <Typography variant="body2">
                                            {summary.totalTime} {timeUnit} / {summary.totalValues} {dataType}
                                        </Typography>
                                        <Typography variant="body2">
                                            {(summary.totalTime / summary.totalValues).toFixed(2)} {timeUnit}/{dataType}
                                        </Typography>
                                    </>
                                ) : (
                                    <>
                                        {/* Mostrar tiempo por línea */}
                                        <Typography variant="body2">
                                            {summary.totalValues} {dataType} / {summary.totalTime.toFixed(2)} {timeUnit}
                                        </Typography>
                                        <Typography variant="body2">
                                            {(summary.totalValues / summary.totalTime).toFixed(2)} {dataType}/{timeUnit}
                                        </Typography>
                                    </>
                                )}
                            </>
                        ) : (
                            <Typography variant="body2">
                                {summary.totalValues} {dataType}
                            </Typography>
                        )}
                    </Box>
                )}

                {/* Gráfico */}
                <Box sx={{ flexGrow: 1 }}>
                    {chartData && chartData.datasets[0].data.length > 0 && chartData.datasets[0].data.some(v => v > 0) ? (
                        <Box sx={{ height: "55vh", marginBottom: 2 }}>
                            <Pie
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
                    ) : (
                        <Typography variant="h6" color="textSecondary" align="center" sx={{ marginTop: 4 }}>
                            No hay datos disponibles para mostrar con los filtros seleccionados.
                        </Typography>
                    )}
                </Box>
            </Box>
        </Box>
    );
};

export default LinePerTimeInIssue;
