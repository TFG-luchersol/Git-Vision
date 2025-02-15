import {
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
  Title,
  Tooltip,
} from "chart.js";
import React, { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import { RiMedalLine } from "react-icons/ri";
import { Input, Table } from "reactstrap";
import tokenService from "../services/token.service";
import getBody from "../util/getBody";
import { darkenColor, stringToColor } from "../util/tools";
import DateRangePicker from "./DateRangePicker";
import "./contributions.css";
  
  ChartJS.register(
    CategoryScale,
    LineElement,
    PointElement,
    LinearScale,
    Title,
    Tooltip,
    Legend,
    LineController
  );
  
  export default function Contributions({owner, repo, path=null}) {
    
    const GoldMedal = <RiMedalLine className="medal gold" />;
    const SilverMedal = <RiMedalLine className="medal silver" />;
    const BronzeMedal = <RiMedalLine className="medal bronze" />;
    const EmptyMedal = <RiMedalLine className="medal dotted" />;
  
    const { username } = tokenService.getUser();
    const [isLoading, setIsLoading] = useState(false);
    const [data_, setData_] = useState({ datasets: [] });
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [contributions, setContributions] = useState([]);
    const [selectedAgrupation, setSelectedAgrupation] = useState("week");
    const [selectedType, setSelectedType] = useState("num_commits");
    const [possibleYears, setPossibleYears] = useState([
      new Date().getFullYear(),
    ]);
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
    const [concreteSample, setConcreteSample] = useState(null);
    const [ranking, setRanking] = useState([]);
    const [typeRanking, setTypeRanking] = useState(true);
  
    // Manejar el cambio del select
    const handleChangeSelectedAgrupation = (event) => {
      const value = event.target.value;
      setSelectedAgrupation((prev) => value);
      document.getElementById("select-concrete").selectedIndex = 0;
      setConcreteSample((prev) => null);
    };
  
    const handleChangeSelectedType = (event) => {
      const value = event.target.value;
      setSelectedType((prev) => value);
    };
  
    const handleChangeSelectedYear = (event) => {
      const value = Number(event.target.value);
      setSelectedYear((prev) => value);
    };
  
    const handleChangeConcreteSample = (event) => {
      const value = event.target.value;
      setConcreteSample((prev) => value || null);
    };
  
    useEffect(() => {}, [concreteSample]);
  
    useEffect(() => {
      generateRanking();
    }, [selectedType, typeRanking]);
  
    useEffect(() => {
      const newDataset = Object.entries(
        groupContributionsByAuthorAndTime(contributions)
      ).map((entry) => ({
        label: entry[0],
        data: entry[1],
        backgroundColor: stringToColor(entry[0]) + "80",
        borderColor: darkenColor(stringToColor(entry[0])),
        borderWidth: 1,
        fill: true,
        showLine: true,
        tension: 0.3,
      }));
      setData_((prev) => ({ datasets: newDataset }));
      generateRanking();
    }, [
      selectedAgrupation,
      selectedType,
      selectedYear,
      concreteSample,
      contributions,
    ]);
  
    const diasSemana = {
      1: "Lunes",
      2: "Martes",
      3: "Miercoles",
      4: "Jueves",
      5: "Viernes",
      6: "Sábado",
      7: "Domingo",
    };
  
    const meses = {
      1: "Enero",
      2: "Febrero",
      3: "Marzo",
      4: "Abril",
      5: "Mayo",
      6: "Junio",
      7: "Julio",
      8: "Agosto",
      9: "Septiembre",
      10: "Octubre",
      11: "Noviembre",
      12: "Diciembre",
    };
  
    function generateRanking() {
      // Crea un mapa para agregar los puntos acumulados por cada autor
      const pointsMap = {};
      let total = 0;
  
      for (const { authorName, additions, deletions } of contributions) {
        if (!pointsMap[authorName]) {
          pointsMap[authorName] = { name: authorName, points: 0 };
        }
  
        let value = 1;
        if (selectedType === "num_additions") {
          value = additions;
        } else if (selectedType === "num_deletions") {
          value = deletions;
        } else if (selectedType === "num_changes") {
          value = additions + deletions;
        }
  
        total += value;
        pointsMap[authorName].points += value;
      }
  
      let newRanking = Object.values(pointsMap).sort(
        (a, b) => b.points - a.points
      );
  
      if (typeRanking) {
        newRanking = newRanking.map((value) => ({
          ...value,
          points: (total === 0 ? 100 : ((value.points / total) * 100)).toFixed(2) + "%", // Convertir a porcentaje con 2 decimales
        }));
      }
      setRanking((prev) => newRanking);
    }
  
    async function getContributions() {
      let url = `/api/v1/contributions/${owner}/${repo}/between_time?login=${username}`;
      if(path !== null) {
        url += `&path=${path}`
      }
      let minYear, maxYear;
      if (startDate !== null) {
        url += `&startDate=${new Date(startDate).toISOString()}`;
        minYear = new Date(startDate).getFullYear();
      }
      if (endDate !== null) {
        url += `&endDate=${new Date(endDate).toISOString()}`;
        maxYear = new Date(endDate).getFullYear();
      } else {
        maxYear = new Date().getFullYear();
      }
  
      try {
        const response = await fetch(url);
        const json = await response.json();
        const result = getBody(json).contributions;
  
        if (minYear === null && result.length !== 0) {
          minYear = result.reduce((earliest, current) => {
            const currentDate = new Date(current.committedDate);
            return currentDate < earliest ? currentDate : earliest;
          }, new Date(result[0].committedDate));
        }
        if (maxYear === null && result.length !== 0) {
          maxYear = result.reduce((earliest, current) => {
            const currentDate = new Date(current.committedDate);
            return currentDate > earliest ? currentDate : earliest;
          }, new Date(result[0].committedDate));
        }
        if (minYear !== null && maxYear !== null) {
          setPossibleYears((prev) =>
            Array.from({ length: maxYear - minYear + 1 }, (_, i) => minYear + i)
          );
        }
        setSelectedYear((prev) => maxYear);
        setContributions((prev) => result);
        generateRanking();
      } catch (error) {
        alert(error);
      }
    }
  
    function groupContributionsByAuthorAndTime(contributions = []) {
      const grouped = {};
      const years = new Set();
  
      contributions.forEach(({ committedDate }) => {
        const date = new Date(committedDate);
        years.add(date.getFullYear());
      });
  
      if (!years.has(selectedYear)) {
        setSelectedYear((prev) => Math.max(...years));
      }
      setPossibleYears((prev) => [...years].sort((a, b) => a - b));
  
      const filtroFecha = (committedDate) => {
        let bool = new Date(committedDate).getFullYear() === selectedYear;
  
        if (concreteSample !== null) {
          if (selectedAgrupation === "week") {
            // Comprobar que tambien se encuentra en el rango de fecha querido
            const [start, end] = getWeeksOfYear()[concreteSample];
            return (
              start <= new Date(committedDate) && new Date(committedDate) <= end
            );
          } else {
            // Comprobar que sea el mes que se quiere
            bool = new Date(committedDate).getMonth() === concreteSample;
          }
        }
  
        return bool;
      };
  
      contributions
        .filter(({ committedDate }) => filtroFecha(committedDate))
        .forEach(({ committedDate, additions, deletions, authorName }) => {
          const date = new Date(committedDate);
  
          let period;
          if (selectedAgrupation === "month") {
            if (concreteSample !== null) {
              // Si concreteSample indica un mes concreto, agrupar por días de ese mes
              period = getDiasEnMes();
            } else {
              // Agrupar por el mes completo si x es null
              period = meses[date.getMonth() + 1];
            }
          } else if (selectedAgrupation === "week") {
            if (concreteSample !== null) {
              // Agrupar por días de esa semana
              period = diasSemana[date.getDay() % 7];
            } else {
              // Agrupar por semana completa si concreteSample es null
              period = String(getWeekOfYear(date));
            }
          }
  
          // Calcular el valor métrico (cambios o commits)
          let value = 1;
          if (selectedType === "num_changes") {
            value = additions + deletions;
          } else if (selectedType === "num_additions") {
            value = additions;
          } else if (selectedType === "num_deletions") {
            value = deletions;
          }
  
          // Inicializar la entrada del autor si no existe
          if (!grouped[authorName]) {
            grouped[authorName] = [];
          }
  
          // Buscar si ya existe un registro para el mismo período
          const existing = grouped[authorName].find(
            (entry) => entry.x === period
          );
  
          if (existing) {
            existing.y += value;
          } else {
            grouped[authorName].push({ x: period, y: value });
          }
        });
      return grouped;
    }
  
    // Función auxiliar para obtener la semana del año
    function getWeekOfYear(date) {
      const currentDate = new Date(date);
  
      const dayOfWeek = (currentDate.getDay() + 6) % 7; // Ajustar para que lunes sea 0
      const startOfYear = new Date(currentDate.getFullYear(), 0, 1);
  
      // Calcular el número de días entre el 1 de enero y la fecha actual
      const daysSinceStartOfYear = Math.floor(
        (currentDate - startOfYear) / (1000 * 60 * 60 * 24)
      );
  
      // Ajustar al inicio de la semana (lunes) y calcular la semana
      const adjustedDays =
        daysSinceStartOfYear + dayOfWeek - ((startOfYear.getDay() + 6) % 7);
  
      const weekNumber = Math.floor(adjustedDays / 7) + 1;
  
      return weekNumber;
    }
  
    async function calcStadistic() {
      setIsLoading((prev) => true);
      await getContributions();
      const newDataset = Object.entries(
        groupContributionsByAuthorAndTime(contributions)
      ).map((entry) => ({
        label: entry[0],
        data: entry[1],
        backgroundColor: stringToColor(entry[0]),
      }));
      setData_((prev) => ({ datasets: newDataset }));
      setIsLoading((prev) => false);
    }
  
    function getWeeksOfYear(year = selectedYear) {
      const weeks = {};
      let weekStart = new Date(year, 0, 1); // 1 de enero del año
      let weekEnd = new Date(weekStart); // Copia de la fecha de inicio para calcular el final de la semana
  
      // Ajustar `weekEnd` para que sea el domingo de la misma semana que `startDate`
      let dayOfWeek = weekStart.getDay() || 7;
      weekEnd.setDate(weekStart.getDate() + (7 - dayOfWeek));
  
      let i = 1;
      while (weekEnd.getFullYear() === year) {
        weeks[i] = [new Date(weekStart), new Date(weekEnd)]; // Copiar las fechas para no mutar objetos
        i++;
  
        // Mover `weekStart` al inicio de la siguiente semana (lunes siguiente)
        if (dayOfWeek !== 1) {
          weekStart.setDate(weekStart.getDate() + (7 - dayOfWeek) + 1);
          dayOfWeek = 1;
        } else {
          weekStart.setDate(weekStart.getDate() + 7);
        }
  
        // Ajustar `weekEnd` al final de la siguiente semana (domingo siguiente)
        weekEnd.setDate(weekEnd.getDate() + 7);
  
        if (
          weekEnd.getFullYear() > year ||
          weekEnd.getTime() === new Date(year, 11, 31).getTime()
        ) {
          weeks[i] = [new Date(weekStart), new Date(year, 11, 31)];
          break;
        }
      }
      return weeks;
    }
  
    const options = {
      responsive: true,
      scales: {
        x: {
          offset: true,
          type: "category",
          labels:
            selectedAgrupation === "week"
              ? concreteSample === null
                ? Object.keys(getWeeksOfYear())
                : Object.values(diasSemana)
              : concreteSample === null
              ? Object.values(meses)
              : getDiasEnMes(),
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
        zoom: {
          zoom: {
            wheel: {
              enabled: true, // Habilita el zoom con la rueda del ratón
            },
            pinch: {
              enabled: true, // Habilita el zoom con gestos táctiles
            },
            mode: "x", // 'x', 'y' o 'xy' para elegir el eje
          },
          pan: {
            enabled: true, // Habilita el desplazamiento
            mode: "x", // 'x', 'y' o 'xy' para elegir el eje
          },
        },
      },
    };
  
    function getDiasEnMes() {
      const num_dias = new Date(selectedYear, concreteSample, 0).getDate();
      return Array.from({ length: num_dias }, (_, i) => 1 + i);
    }
  
    const representarRangoFechas = (fechas) => {
      const [fecha_1, fecha_2] = fechas;
      const formattedStartDate = fecha_1.toLocaleDateString("es-ES");
      const formattedEndDate = fecha_2.toLocaleDateString("es-ES");
      return `${formattedStartDate} - ${formattedEndDate}`;
    };
  
    return (
      <div style={{ display: "flex", flexDirection: "row" }}>
        <div style={{ display: "flex", flexDirection: "column" }}>
          <div style={{ display: "flex", flexDirection: "row", marginLeft: 70 }}>
            <DateRangePicker
              startDate={startDate}
              endDate={endDate}
              setStartDate={setStartDate}
              setEndDate={setEndDate}
            />
            <button
              onClick={calcStadistic}
              disabled={isLoading}
              className="button" // Usamos la clase de botón desde el CSS
              style={{ marginLeft: 100 }}
            >
              {isLoading ? (
                <div className="spinner"></div> // Usamos la clase de espiral desde el CSS
              ) : (
                "Buscar"
              )}
            </button>
          </div>
  
          <div style={{ display: "flex", flexDirection: "row" }}>
            <div style={{ height: "55vh", width: "100vh" }}>
              <Line data={data_} options={options} />
            </div>
  
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                marginLeft: 40,
              }}
            >
              {contributions.length > 0 && (
                <>
                  <p style={{ marginTop: 10, marginBottom: 0 }}>Año:</p>
                  <input
                    value={selectedYear}
                    onChange={handleChangeSelectedYear}
                    min={possibleYears[0]}
                    max={possibleYears[possibleYears.length - 1]}
                    type="number"
                  />
                </>
              )}
              <p style={{ marginTop: 10, marginBottom: 0 }}>Tipo de filtrado:</p>
              <select
                id="select-type"
                value={selectedType}
                onChange={handleChangeSelectedType}
              >
                <option value="num_commits">Nº Commits</option>
                <option value="num_changes">Nº Cambios</option>
                <option value="num_additions">Nº Lineas Añadidas</option>
                <option value="num_deletions">Nº Lineas Borradas</option>
              </select>
              <p style={{ marginTop: 10, marginBottom: 0 }}>
                Tipo de agrupamiento:
              </p>
              <select
                id="select-interval"
                value={selectedAgrupation}
                onChange={handleChangeSelectedAgrupation}
              >
                <option value="week">Semanas</option>
                <option value="month">Meses</option>
              </select>
              <p style={{ marginTop: 10, marginBottom: 0 }}>Muestra concreta:</p>
              <select
                id="select-concrete"
                value={concreteSample}
                onChange={handleChangeConcreteSample}
              >
                <option value="" selected>
                  {" "}
                  ────────{" "}
                </option>
                {selectedAgrupation === "month"
                  ? Object.entries(meses).map((entry) => (
                      <option value={entry[0]}>{entry[1]}</option>
                    ))
                  : Object.entries(getWeeksOfYear()).map((entry) => (
                      <option value={entry[0]}>
                        {representarRangoFechas(entry[1])}
                      </option>
                    ))}
              </select>
            </div>
          </div>
        </div>
        <div style={{ width: "70vh" }}>
          <div style={{ display: "flex", justifyContent: "center" }}>
            <p>Ranking</p>
          </div>
          <div style={{ height: "45%", overflowY: "auto", marginInline: 10 }}>
            <Table striped bordered responsive>
              <thead>
                <tr>
                  <th>Medalla</th>
                  <th>Nombre</th>
                  <th>Puntos</th>
                </tr>
              </thead>
              <tbody>
                {ranking.map((user, index) => (
                  <tr key={index}>
                    <td>
                      {index === 0
                        ? GoldMedal
                        : index === 1
                        ? SilverMedal
                        : index === 2
                        ? BronzeMedal
                        : EmptyMedal}
                    </td>
                    <td>{user.name}</td>
                    <td>{user.points}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
          <div style={{ display: "flex", flexDirection: "row", marginTop: 10, marginLeft: 10 }}>
            <p>PORCENTAJE:</p>
            <Input
              checked={typeRanking}
              style={{ marginLeft: 10 }}
              type="checkbox"
              onChange={() => setTypeRanking((prev) => !prev)}
            />
          </div>
        </div>
      </div>
    );
  }
  