import { CalendarMonth } from 'cally';
import {
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    PointElement,
    Title,
    Tooltip,
} from "chart.js";
import React, { useEffect, useState } from "react";
import { Scatter } from "react-chartjs-2";
import { useParams } from "react-router-dom";
import tokenService from "../../../../services/token.service";
import DateRangePicker from '../../../../components/DateRangePicker';

ChartJS.register(Title, Tooltip, Legend, PointElement, CategoryScale, LinearScale);

export default function CommitsByTime() {
    const BY_NUM_COMMITS = "Nº commits";
    const BY_NUM_CHANGES = "Nº cambios";

    const { username } = tokenService.getUser();
    const { owner, repo } = useParams();
    const [data_, setData_] = useState({ datasets: [] })
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [contributions, setContributions] = useState([])

    const range = (start, end) => Array.from({ length: end - start + 1 }, (_, i) => start + i);

    const meses = {
        1: "Enero", 2: "Febrero", 3: "Marzo", 4: "Abril", 5: "Mayo", 6: "Junio",
        7: "Julio", 8: "Agosto", 9: "Septiembre", 10: "Octubre", 11: "Noviembre", 12: "Diciembre"
    }

    function stringToColor(str) {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            hash = str.charCodeAt(i) + ((hash << 5) - hash);
        }

        // Extraer los componentes RGB
        let r = (hash >> 16) & 0xFF;
        let g = (hash >> 8) & 0xFF;
        let b = hash & 0xFF;

        r = (r * 2) % 256;
        g = (g * 2) % 256;
        b = (b * 2) % 256;

        r = Math.min(255, r + 50);
        g = Math.min(255, g + 50);
        b = Math.min(255, b + 50);

        r = Math.max(128, r);
        g = Math.max(128, g);
        b = Math.max(128, b);

        let color = '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1).toUpperCase();

        return color;
    }

    useEffect(() => {

    }, [])

    async function getContributions() {
        let url = `/api/v1/commits/${owner}/${repo}/between_time?login=${username}`
        url += startDate !== null ? `&startDate=${startDate}` : "";
        url += endDate !== null ? `&endDate=${endDate}` : "";
        try {
            const response = await fetch(url);
            const json = await response.json();
            setContributions(json.information.information.contributions)
        } catch (error) {
            alert(error)
        }
    }

    function parseFecha(fechasString, tipo) {
        for (const contribution of contributions) {
            console.log(contribution)
        }
        return { x: 1, y: 2 }
        // let res = {"x": null, "y": null}
        // for(const fechaString in fechasString) {
        //     const [year, month, day] = fechaString.split("-").map(Number);
        // }

        // return meses[month]
    }

    async function getNumCommitsByUserInTime() {
        // getContributions();
        // const newDataset = contributions.map(contribution => ({
        //     "label": contribution.authorName,
        //     "data": parseFecha(contribution),
        //     "backgroundColor": stringToColor(contribution.authorName)
        // }));
        // setData_({ "datasets": newDataset })
    }

    async function getNumChangesByUserInTime() {
        console.log(getWeeksOfYear(2025))
        // getContributions();
        // const newDataset = contributions.map(contribution => ({
        //     "label": contribution.authorName,
        //     "data": parseFecha(contribution),
        //     "backgroundColor": stringToColor(contribution.authorName)
        // }));
        // setData_({ "datasets": newDataset })

    }

    function getWeeksOfYear(year) {
        const weeks = {};
        let startDate = new Date(year, 0, 1); // 1 de enero del año
        let endDate = new Date(year, 11, 31); // 31 de diciembre del año
        let i = 1;
        while (startDate <= endDate) {
            let weekStart = new Date(startDate);
            let weekEnd = new Date(startDate);
            weekEnd.setDate(weekEnd.getDate() + 6); // Agregar 6 días para completar la semana

            if (weekEnd > endDate) {
                weekEnd = endDate; // Ajustar la última semana al final del año
            }

            weeks[i] = [weekStart, weekEnd];
            i++;

            startDate.setDate(startDate.getDate() + 7); // Mover al inicio de la siguiente semana
        }

        return weeks;
    }


    const action = async () => {
        const selectComponent = document.getElementById("select-type");
        const type = selectComponent.value;
        if (type === BY_NUM_COMMITS) {
            getNumCommitsByUserInTime();
        } else if (type === BY_NUM_CHANGES) {
            getNumChangesByUserInTime();
        }
    }

    // const data = {
    //     datasets: [
    //         {
    //             label: "Juan",
    //             data: [
    //                 { x: "Enero", y: 3 },
    //                 { x: "Enero", y: 5 },
    //                 { x: "Febrero", y: 8 },
    //             ],
    //             backgroundColor: "red",
    //         },
    //         {
    //             label: "Manuel",
    //             data: [
    //                 { x: "Enero", y: 2 },
    //                 { x: "Febrero", y: 6 },
    //                 { x: "Febrero", y: 9 },
    //             ],
    //             backgroundColor: "blue",
    //         },
    //     ],
    // };

    const options = {
        responsive: true,
        scales: {
            x: {
                type: "category",
                labels: false ? Object.keys(getWeeksOfYear(2025)) : Object.values(meses),
                title: {
                    display: true,
                    text: "Tiempo",
                },
            },
            y: {
                title: {
                    display: true,
                    text: "Commits / Ítems",
                },
                beginAtZero: true,
            },
        },
        plugins: {
            legend: {
                position: "right",
            },
            title: {
                display: true,
                text: "Commits por Usuario",
            },
        },
    };

    return (
        <div style={{ display: "flex", flexDirection: "column" }}>
            <Scatter data={data_} options={options} />
            <DateRangePicker
                startDate={startDate}
                endDate={endDate}
                handleStartDateChange={setStartDate}
                handleEndDateChange={setEndDate}
            />
            <div>
                <select id="select-type">
                    <option>{BY_NUM_COMMITS}</option>
                    <option>{BY_NUM_CHANGES}</option>
                </select>
                <button>
                    Buscar
                </button>
            </div>
            <div>
                <select id="select-interval">
                    <option>Semanas</option>
                    <option>Meses</option>
                </select>
                <button>
                    Buscar
                </button>
            </div>
            {/* <div>
                <select id="select-type">
                    {range(startDate, endDate).map(date =>
                        <option>{new Date(date).getFullYear()}</option>
                    )}
                </select>
                <button>
                    Buscar
                </button>
            </div> */}
        </div>
    );
};