import React from "react";
import { Bar, Line, Pie } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    ArcElement,
    Tooltip,
    Legend,
    Title,
} from "chart.js";

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    ArcElement,
    Tooltip,
    Legend,
    Title
);

const StatsTrends = ({ data }) => {
    // Preprocess data
    const groupedByMonth = {};

    data.forEach((entry) => {
        const month = entry.date.slice(0, 7); // 'YYYY-MM'
        if (!groupedByMonth[month]) {
            groupedByMonth[month] = {
                amount: 0,
                carbonOffset: 0,
                panels: 0,
            };
        }
        groupedByMonth[month].amount += entry.amount;
        groupedByMonth[month].carbonOffset += parseFloat(entry.carbonOffset);
        groupedByMonth[month].panels += entry.quantity;
    });

    const labels = Object.keys(groupedByMonth).sort();
    const monthlyDonations = labels.map((m) => groupedByMonth[m].amount);
    const monthlyCarbonOffsets = labels.map((m) => groupedByMonth[m].carbonOffset);
    const monthlyPanels = labels.map((m) => groupedByMonth[m].panels);

    // Totals for KPI cards
    const totalDonations = monthlyDonations.reduce((a, b) => a + b, 0);
    const totalCarbonOffset = monthlyCarbonOffsets.reduce((a, b) => a + b, 0);
    const totalPanels = monthlyPanels.reduce((a, b) => a + b, 0);

    const previousDonations = monthlyDonations.length > 1 ? monthlyDonations[monthlyDonations.length - 2] : 0;
    const donationGrowth = previousDonations > 0 ? ((totalDonations - previousDonations) / previousDonations) * 100 : 0;

    const previousPanels = monthlyPanels.length > 1 ? monthlyPanels[monthlyPanels.length - 2] : 0;
    const panelsGrowth = previousPanels > 0 ? ((totalPanels - previousPanels) / previousPanels) * 100 : 0;

    // Bar chart for Monthly Donations (simplified)
    const barChartData = {
        labels,
        datasets: [
            {
                label: "Monthly Donations (£)",
                data: monthlyDonations,
                backgroundColor: "rgba(75, 192, 192, 0.7)",
            },
        ],
    };

    // Pie chart for Carbon Offset distribution by month
    const pieCarbonData = {
        labels,
        datasets: [
            {
                label: "Carbon Offset Distribution ",
                data: monthlyCarbonOffsets,
                backgroundColor: [
                    "rgba(153, 102, 255, 0.7)",
                    "rgba(201, 203, 207, 0.7)",
                    "rgba(255, 159, 64, 0.7)",
                    "rgba(54, 162, 235, 0.7)",
                    "rgba(255, 99, 132, 0.7)",
                    "rgba(255, 205, 86, 0.7)",
                    "rgba(75, 192, 192, 0.7)",
                    "rgba(153, 102, 255, 0.7)",
                    "rgba(201, 203, 207, 0.7)",
                    "rgba(255, 159, 64, 0.7)",
                    "rgba(54, 162, 235, 0.7)",
                    "rgba(255, 99, 132, 0.7)",
                ],
                borderWidth: 1,
            },
        ],
    };

    // Line chart for Panels Deployed
    const linePanelsData = {
        labels,
        datasets: [
            {
                label: "Panels Deployed",
                data: monthlyPanels,
                borderColor: "rgba(255, 159, 64, 1)",
                backgroundColor: "rgba(255, 159, 64, 0.3)",
                fill: true,
                tension: 0.3,
            },
        ],
    };

    // Dummy icon placeholders as colored circles
    const Icon = ({ color }) => (
        <div style={{
            width: 30,
            height: 30,
            borderRadius: "50%",
            backgroundColor: color,
            display: "inline-block",
            marginRight: 10,
            verticalAlign: "middle",
        }} />
    );

    return (
        <div className="container my-4">

            {/* Trends Overview Section */}
            <div className="mb-4">
                <h4 className="mb-4 fw-semibold">Trends Overview</h4>
                <div className="row g-4">
                    <div className="col-md-4" style={{ width: "400px" }}>
                        <div className="card rounded-3 shadow-sm p-3" style={{minHeight: '400px'}}>
                            <div className="mb-3">
                                <h6 className="fw-semibold">Gross Volume</h6>
                                <div className="d-flex align-items-center">
                                    <h4 className="mb-0 fw-bold">£{totalDonations.toLocaleString()}</h4>
                                   <span
                                        className={`badge ms-2 ${donationGrowth >= 0 ? 'bg-success' : 'bg-danger'}`}
                                        title="Cumulative growth compared to previous month"
                                        style={{ cursor: 'pointer' }}
                                    >
                                        {donationGrowth >= 0 ? "+" : ""}{donationGrowth.toFixed(2)}%
                                    </span>
                                </div>
                                <small className="text-muted">£{previousDonations.toLocaleString()} previous period</small>
                            </div>
                            <Bar data={barChartData} options={{
                                responsive: true,
                                plugins: {
                                    legend: { display: false },
                                    tooltip: { enabled: true },
                                },
                                scales: {
                                    x: {
                                        title: {
                                            display: false,
                                        },
                                    },
                                    y: {
                                        title: {
                                            display: false,
                                        },
                                        beginAtZero: true,
                                    },
                                },
                            }} height={150} />
                            <div className="d-flex justify-content-between align-items-center mt-2">
                                {/*<a href="#" className="text-primary small">View more</a>*/}
                                <small className="text-muted">Updated {new Date().toLocaleTimeString()}</small>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4" style={{ width: "400px" }}>
                        <div className="card rounded-3 shadow-sm p-3 d-flex flex-column align-items-center" style={{minHeight: '400px'}}>
                            <div className="d-flex align-items-center mb-3 w-100">
                                <Icon color="#9966ff" />
                                <h6 className="mb-0 fw-semibold">Carbon Offset Distribution</h6>
                            </div>
                            <div style={{ width: '100%', height: '280px' }}>
                                <small className="text-muted mb-3">Per Year Distribution</small>
                                <Pie data={pieCarbonData} options={{
                                    responsive: true,
                                    plugins: {
                                        legend: { position: "bottom" },
                                        tooltip: { enabled: true },
                                    },
                                }} />
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card rounded-3 shadow-sm p-3" style={{minHeight: '400px'}}>
                            <div className="mb-3">
                                <h6 className="fw-semibold">Panels Deployed Over Time</h6>
                                <div className="d-flex align-items-center">
                                    <h4 className="mb-0 fw-bold">{totalPanels.toLocaleString()}</h4>
                                    <span className={`badge ms-2 ${panelsGrowth >= 0 ? 'bg-success' : 'bg-danger'}`}>
                                        {panelsGrowth >= 0 ? "+" : ""}{panelsGrowth.toFixed(2)}%
                                    </span>
                                </div>
                                <small className="text-muted">{previousPanels.toLocaleString()} previous period</small>
                            </div>
                            <Line data={linePanelsData} options={{
                                responsive: true,
                                plugins: {
                                    legend: { display: false },
                                    tooltip: { enabled: true },
                                },
                                scales: {
                                    x: {
                                        title: {
                                            display: false,
                                        },
                                    },
                                    y: {
                                        title: {
                                            display: false,
                                        },
                                        beginAtZero: true,
                                    },
                                },
                            }} height={150} />
                            <div className="d-flex justify-content-between align-items-center mt-2">
                                {/*<a href="#" className="text-primary small">View more</a>*/}
                                <small className="text-muted">Updated {new Date().toLocaleTimeString()}</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default StatsTrends;
