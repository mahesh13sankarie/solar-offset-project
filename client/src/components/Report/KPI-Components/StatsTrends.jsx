import React from "react";
import { Line, Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
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
    const monthlyCarbonOffsets = labels.reduce((acc, m, i) => {
        const prev = i > 0 ? acc[i - 1] : 0;
        acc.push(prev + groupedByMonth[m].carbonOffset);
        return acc;
    }, []);
    const monthlyPanels = labels.map((m) => groupedByMonth[m].panels);

    const barChartData = {
        labels,
        datasets: [
            {
                label: "Monthly Donations (£)",
                data: monthlyDonations,
                backgroundColor: "rgba(75, 192, 192, 0.6)",
            },
        ],
    };

    const lineCarbonData = {
        labels,
        datasets: [
            {
                label: "Cumulative Carbon Offset (tons)",
                data: monthlyCarbonOffsets,
                borderColor: "rgba(153, 102, 255, 1)",
                backgroundColor: "rgba(153, 102, 255, 0.2)",
                fill: true,
                tension: 0.3,
            },
        ],
    };

    const linePanelsData = {
        labels,
        datasets: [
            {
                label: "Panels Deployed",
                data: monthlyPanels,
                borderColor: "rgba(255, 159, 64, 1)",
                backgroundColor: "rgba(255, 159, 64, 0.2)",
                fill: true,
                tension: 0.3,
            },
        ],
    };

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6 p-3">
                    <div style={{ width: '100%', height: '300px' }}>
                        <h2 className="text-xl font-semibold mb-2">Monthly Donations</h2>
                        <Bar data={barChartData} options={{
                            responsive: true,
                            plugins: {
                                title: {
                                    display: false,
                                },
                            },
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Month',
                                        position: 'bottom',
                                    },
                                },
                                y: {
                                    title: {
                                        display: true,
                                        text: 'Donations (£)',
                                        position: 'left',
                                    },
                                },
                            },
                        }} />
                    </div>
                </div>
                <div className="col-md-6 p-3">
                    <div style={{ width: '100%', height: '330px' }}>
                        <h2 className="text-xl font-semibold mb-2">Carbon Offset Growth</h2>
                        <Line data={lineCarbonData} options={{
                            responsive: true,
                            plugins: {
                                title: {
                                    display: false,
                                },
                            },
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Month',
                                        position: 'bottom',
                                    },
                                },
                                y: {
                                    title: {
                                        display: true,
                                        text: 'Carbon Offset (tons)',
                                        position: 'left',
                                    },
                                },
                            },
                        }} />
                    </div>
                </div>
            </div>
            <div className="row justify-content-center mt-4">
                <div className="col-md-6 d-flex justify-content-center">
                    <div style={{ width: '100%', height: '300px' }}>
                        <h2 className="text-xl font-semibold mb-2">Panels Deployed Over Time</h2>
                        <Line data={linePanelsData} options={{
                            responsive: true,
                            plugins: {
                                title: {
                                    display: false,
                                },
                            },
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Month',
                                        position: 'bottom',
                                    },
                                },
                                y: {
                                    title: {
                                        display: true,
                                        text: 'Panels Count',
                                        position: 'left',
                                    },
                                },
                            },
                        }} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default StatsTrends;
