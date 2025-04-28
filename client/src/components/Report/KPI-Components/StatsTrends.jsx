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
                label: "Carbon Offset Distribution",
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
                    <div className="col-md-4">
                        <div className="card rounded-3 shadow-sm p-3" style={{minHeight: '360px'}}>
                            <div className="d-flex align-items-center mb-3">
                                <Icon color="#4bc0c0" />
                                <h6 className="mb-0 fw-semibold">Monthly Donations</h6>
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
                            }} />
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card rounded-3 shadow-sm p-3 d-flex flex-column align-items-center" style={{minHeight: '360px'}}>
                            <div className="d-flex align-items-center mb-3 w-100">
                                <Icon color="#9966ff" />
                                <h6 className="mb-0 fw-semibold">Carbon Offset Distribution</h6>
                            </div>
                            <div style={{ width: '100%', height: '280px' }}>
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
                        <div className="card rounded-3 shadow-sm p-3" style={{minHeight: '360px'}}>
                            <div className="d-flex align-items-center mb-3">
                                <Icon color="#ff9f40" />
                                <h6 className="mb-0 fw-semibold">Panels Deployed Over Time</h6>
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
                            }} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default StatsTrends;
