
import React, { useState } from "react";
import "./dashboard-theme.css";

const countryData = {
    zone: "CA",
    country: "Canada",
    carbonEmissions: -1298.08,
    electricityAvailability: 1762.0,
    solarPowerPotential: 1.51,
    renewablePercentage: 68,
    population: 38,
    solarPanels: [
        {
            id: 13,
            panelName: "SunPower Maxeon 7DC 445W",
            installationCost: 400.0,
            productionPerPanel: 445.0,
            description:
                "Top-tier efficiency, extremely durable (40-year warranty), compact and space-saving, excellent sustainability profile",
            countryCode: "CA",
        },
        {
            id: 14,
            panelName: "AIKO ABC Neostar 3N54 495W",
            installationCost: 390.0,
            productionPerPanel: 495.0,
            description:
                "Highest production capacity on this list, advanced cell technology, standard warranty coverage, efficient for high-demand households",
            countryCode: "CA",
        },
        {
            id: 15,
            panelName: "REC Alpha Pure RX Series 470W",
            installationCost: 370.0,
            productionPerPanel: 470.0,
            description:
                "Good overall performance, solid durability and sustainability rating, aesthetic all-black design",
            countryCode: "CA",
        },
        {
            id: 28,
            panelName: "SunPower Maxeon 7DC 445W",
            installationCost: 400.0,
            productionPerPanel: 445.0,
            description:
                "Top-tier efficiency, extremely durable (40-year warranty), compact and space-saving, excellent sustainability profile",
            countryCode: "CA",
        },
    ],
};

function PanelFunding({ panel }) {
    const [count, setCount] = useState(1);
    return (
        <div className="card panel-card mb-4">
            <div className="card-body">
                <h5 className="card-title" style={{ color: "var(--primary-dark)" }}>
                    {panel.panelName}
                </h5>
                <p className="card-text">{panel.description}</p>
                <p className="fw-semibold mb-2">
                    Installation Cost: <span style={{ color: "var(--primary)" }}>${panel.installationCost}</span>
                </p>
                <div className="d-flex align-items-center mb-2">
                    <button
                        className="btn btn-outline-primary btn-sm"
                        onClick={() => setCount(Math.max(1, count - 1))}
                    >
                        -
                    </button>
                    <span className="mx-3">{count}</span>
                    <button
                        className="btn btn-primary btn-sm"
                        onClick={() => setCount(count + 1)}
                    >
                        +
                    </button>
                </div>
                <button
                    className="btn btn-fund w-100"
                    style={{ background: "var(--primary)", color: "#fff" }}
                    onClick={() =>
                        alert(
                            `Thank you for funding ${count} ${panel.panelName}${count > 1 ? "s" : ""}!`
                        )
                    }
                >
                    Fund Now · ${panel.installationCost * count}
                </button>
            </div>
        </div>
    );
}

export default function CountryDashboard() {
    const data = countryData;
    return (
        <div className="container py-4">
            <div className="dashboard-hero p-4 mb-4 rounded" style={{ background: "var(--primary-light)" }}>
                <h1 className="display-5 mb-2" style={{ color: "#fff" }}>
                    Support Solar in <span className="fw-bold">{data.country}</span>
                </h1>
                <p style={{ color: "var(--primary-pale)" }}>
                    Help fund clean energy through the solar panel(s) of your choice!
                </p>
            </div>
            <div className="row mb-4">
                <div className="col-lg-3 mb-2">
                    <div className="info-card">
                        <div className="info-label">Carbon Emissions</div>
                        <div className="info-value">{data.carbonEmissions} kt</div>
                    </div>
                </div>
                <div className="col-lg-3 mb-2">
                    <div className="info-card">
                        <div className="info-label">Electricity Avail.</div>
                        <div className="info-value">{data.electricityAvailability} MWh</div>
                    </div>
                </div>
                <div className="col-lg-3 mb-2">
                    <div className="info-card">
                        <div className="info-label">Solar Potential</div>
                        <div className="info-value">{data.solarPowerPotential} kWh/m²</div>
                    </div>
                </div>
                <div className="col-lg-3 mb-2">
                    <div className="info-card">
                        <div className="info-label">Renewables</div>
                        <div className="info-value">{data.renewablePercentage}%</div>
                    </div>
                </div>
            </div>
            <div className="row">
                {data.solarPanels.map(panel => (
                    <div className="col-md-6 col-lg-4" key={panel.id}>
                        <PanelFunding panel={panel} />
                    </div>
                ))}
            </div>
        </div>
    );
}
