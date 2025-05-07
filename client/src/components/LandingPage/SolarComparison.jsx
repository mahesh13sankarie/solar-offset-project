import Navbar from "./Navbar.jsx";
import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import { Link } from "react-router-dom";
import { BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip } from "chart.js";
import { api } from "../../api";


ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// Color palette
const COLORS = [
    "rgba(255, 99, 132, 0.7)",
    "rgba(54, 162, 235, 0.7)",
    "rgba(255, 206, 86, 0.7)",
    "rgba(75, 192, 192, 0.7)",
    "rgba(153, 102, 255, 0.7)",
];

const SolarComparison = () => {
    const [countries, setCountries] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            const res = await api.countries.getAllCountries();
            setCountries(res.data);
        };

        fetchData();
    }, []);

    const filtered = countries.filter((item) =>
        item.country.toLowerCase().includes(search.toLowerCase()),
    );

    const chartOptions = (title) => ({
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
            title: { display: true, text: title },
            tooltip: {
                callbacks: {
                    label: (ctx) => `${ctx.dataset.label}: ${ctx.formattedValue}`,
                },
            },
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback: (val) => val.toLocaleString(),
                },
            },
        },
    });

    const carbonChart = {
        labels: filtered.map((c) => c.country),
        datasets: [
            {
                label: "Carbon Emissions (tCO₂)",
                data: filtered.map((c) => c.carbonEmissions),
                backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length]),
            },
        ],
    };

    const electricityChart = {
        labels: filtered.map((c) => c.country),
        datasets: [
            {
                label: "Electricity Availability (MWh)",
                data: filtered.map((c) => c.electricityAvailability),
                backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length]),
            },
        ],
    };

    const carbonIntensityChart = {
        labels: filtered.map((c) => c.country),
        datasets: [
            {
                label: "tCO₂ per MWh",
                data: filtered.map((c) =>
                    c.electricityAvailability > 0
                        ? (c.carbonEmissions / c.electricityAvailability).toFixed(2)
                        : 0,
                ),
                backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length]),
            },
        ],
    };


    const renewablesChart = {
        labels: filtered.map((c) => c.country),
        datasets: [
            {
                label: "Renewable Energy (%)",
                data: filtered.map((c) => c.renewablePercentage),
                backgroundColor: filtered.map((_, i) => COLORS[i % COLORS.length]),
            },
        ],
    };

    const countryScores = countries.map((item) => {
        const emission = item.carbonEmissions;
        const renewable = item.renewablePercentage;
        const solar = item.solarPowerPotential;

        const emissionScore = emission; // Bisa distandarisasi jika perlu
        const renewableScore = 100 - renewable;
        const solarScore = 100 - solar;

        const totalScore = emissionScore * 0.4 + renewableScore * 0.3 + solarScore * 0.3;

        return {
            country: item.country,
            score: totalScore.toFixed(2)
        };
    });

    const getRecommendedCountry = () => {
        const recommended = countryScores.sort((a, b) => b.score - a.score)[0];
        console.log("recomended country",recommended.country)
        return recommended.country;
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
            case "solarPowerPotential":
                return `${value.toFixed(2)}%`;
            default:
                return value;
        }
    };

    return (
        <div>
            <Navbar />
            <section className="container mt-4">
                <h2 className="my-4">Solar Comparison</h2>

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
                        <th>tCO₂ per MWh</th>
                        <th>Renewables</th>
                        <th>Existing Solar Production</th>
                        <th>Population</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filtered.map((item, index) => (
                        <tr key={item.zone} style={{ verticalAlign: "middle" }}>
                            <td>{item.country}

                                {item.country === getRecommendedCountry() && (
                                    <span
                                        style={{
                                            display: "inline-block",
                                            fontSize: "0.75rem",
                                            padding: "0.35em 0.65em",
                                            fontWeight: "600",
                                            lineHeight: "1",
                                            color: "#fff",
                                            backgroundColor: "#17a2b8",
                                            borderRadius: "10px",
                                            marginLeft: "8px"}}
                                        className="badge badge-pill badge-info">Recommended</span>
                                )

                                }
                            </td>

                            <td>{formatValue("carbonEmissions", item.carbonEmissions)}</td>
                            <td>
                                {formatValue(
                                    "electricityAvailability",
                                    item.electricityAvailability,
                                )}
                            </td>
                            <td>
                                {item.electricityAvailability > 0
                                    ? (item.carbonEmissions / item.electricityAvailability).toFixed(2)
                                    : "-"}
                            </td>
                            <td>
                                {formatValue("renewablePercentage", item.renewablePercentage)}
                            </td>
                            <td>
                                {formatValue("solarPowerPotential", item.solarPowerPotential)}
                            </td>
                            <td>{formatValue("population", item.population)}</td>
                            <td>
                                <Link
                                    to={`/InstallationCost/${item.zone}`}
                                    className="btn btn-outline-primary btn-sm d-flex align-items-center justify-content-center gap-1"
                                    style={{
                                        fontWeight: 600,
                                        fontSize: "0.9rem",
                                        minWidth: "120px",
                                        borderRadius: "0.5rem",
                                        padding: "6px 12px",
                                    }}
                                >
                                    <i
                                        className="bi bi-arrow-right-circle"
                                        style={{ fontSize: "1rem" }}
                                    ></i>
                                    Details
                                </Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <div><p style={{fontSize: "0.85rem"}}><span className="bi-info-circle"></span> A country with high
                    carbon emissions, low renewable energy production, and low solar production is the
                    recommended country to receive donations.
                </p>
                </div>
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
                                <span style={{ fontWeight: 500 }}>{item.country}</span>
                            </div>
                        ))}
                    </div>
                )}

                {/* Charts */}
                <div className="row mb-5">
                    <div className="col-md-4 mb-4" style={{ height: "400px" }}>
                        <Bar data={carbonChart} options={chartOptions("Carbon Emissions (tCO₂)")} />
                    </div>
                    <div className="col-md-4 mb-4" style={{ height: "400px" }}>
                        <Bar
                            data={electricityChart}
                            options={chartOptions("Electricity Availability (MWh)")}
                        />
                    </div>
                    <div className="col-md-4 mb-4" style={{ height: "400px" }}>
                        <Bar
                            data={renewablesChart}
                            options={chartOptions("Renewable Energy (%)")}
                        />
                    </div>
                </div>
            </section>
        </div>
    );
};

export default SolarComparison;
