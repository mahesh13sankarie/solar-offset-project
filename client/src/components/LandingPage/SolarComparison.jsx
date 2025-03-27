import Navbar from "./Navbar.jsx";
import React, {useEffect, useState} from "react";
import {Bar} from "react-chartjs-2";
import {Link} from "react-router-dom";
import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// Color palette
const COLORS = [
    "rgba(255, 99, 132, 0.7)",
    "rgba(54, 162, 235, 0.7)",
    "rgba(255, 206, 86, 0.7)",
    "rgba(75, 192, 192, 0.7)",
    "rgba(153, 102, 255, 0.7)"
];

const SolarComparison = () => {
    const [countries, setCountries] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await fetch("http://localhost:8000/api/v1/countries");
                const json = await res.json();
                setCountries(json);
            } catch (err) {
                console.error("Failed to fetch data:", err);
            }
        };
        fetchData();
    }, []);

    const filtered = countries.filter((item) =>
        item.country.toLowerCase().includes(search.toLowerCase())
    );

    const chartOptions = (title) => ({
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {display: false},
            title: {display: true, text: title},
            tooltip: {
                callbacks: {
                    label: (ctx) => `${ctx.dataset.label}: ${ctx.formattedValue}`
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback: val => val.toLocaleString()
                }
            }
        }
    });

    const carbonChart = {
        labels: filtered.map(c => c.country),
        datasets: [{
            label: "Carbon Emissions (tCO₂)",
            data: filtered.map(c => c.carbonEmissions),
            backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length])
        }]
    };

    const electricityChart = {
        labels: filtered.map(c => c.country),
        datasets: [{
            label: "Electricity Availability (MWh)",
            data: filtered.map(c => c.electricityAvailability),
            backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length])
        }]
    };

    const renewablesChart = {
        labels: filtered.map(c => c.country),
        datasets: [{
            label: "Renewable Energy (%)",
            data: filtered.map(c => c.renewablePercentage),
            backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length])
        }]
    };

    const formatValue = (key, value) => {
        if (value == null) return "-";
        switch (key) {
            case "carbonEmissions":
                return `${value.toLocaleString()} tCO₂`;
            case "electricityAvailability":
                return `${value.toLocaleString()} MWh`;
            case "renewablePercentage":
                return `${value.toFixed(2)}%`;
            case "population":
                return `${value} million`;
            default:
                return value;
        }
    };

    return (
        <div>
            <Navbar/>
            <section className="container mt-4">
                <h2>Solar Comparison</h2>

                {/* Search */}
                <input
                    type="text"
                    className="form-control mb-3 w-25"
                    placeholder="Search country..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

                {/* Table */}
                <table className="table table-bordered table-striped mb-3">
                    <thead className="table-light">
                    <tr>
                        <th>Country</th>
                        <th>Carbon Emissions</th>
                        <th>Electricity</th>
                        <th>Renewables</th>
                        <th>Population</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filtered.map((item, index) => (
                        <tr key={item.zone} style={{verticalAlign: "middle"}}>
                            <td>{item.country}</td>
                            <td>{formatValue("carbonEmissions", item.carbonEmissions)}</td>
                            <td>{formatValue("electricityAvailability", item.electricityAvailability)}</td>
                            <td>{formatValue("renewablePercentage", item.renewablePercentage)}</td>
                            <td>{formatValue("population", item.population)}</td>
                            <td>
                                <div className="d-flex gap-1">
                                    <Link to={`/InstallationCost/${item.zone}`}>
                                        <button className="btn btn-outline-success btn-sm">
                                            <i className="bi bi-info-circle"></i> Details
                                        </button>
                                    </Link>
                                    <button className="btn btn-outline-primary btn-sm">
                                        <i className="bi bi-heart-fill"></i> Donate
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                {/* Color Legend */}
                {filtered.length > 0 && (
                    <div className="d-flex flex-wrap justify-content-center gap-4 mb-4 mt-4">
                        {filtered.map((item, index) => (
                            <div key={item.zone} className="d-flex align-items-center gap-2">
                                <div
                                    style={{
                                        width: 16,
                                        height: 16,
                                        backgroundColor: COLORS[index % COLORS.length],
                                        borderRadius: 3,
                                    }}
                                ></div>
                                <span style={{fontWeight: 500}}>{item.country}</span>
                            </div>
                        ))}
                    </div>
                )}

                {/* Charts */}
                <div className="row mb-5">
                    <div className="col-md-4 mb-4" style={{height: "400px"}}>
                        <Bar data={carbonChart} options={chartOptions("Carbon Emissions")}/>
                    </div>
                    <div className="col-md-4 mb-4" style={{height: "400px"}}>
                        <Bar data={electricityChart} options={chartOptions("Electricity Availability")}/>
                    </div>
                    <div className="col-md-4 mb-4" style={{height: "400px"}}>
                        <Bar data={renewablesChart} options={chartOptions("Renewable Energy %")}/>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default SolarComparison;

