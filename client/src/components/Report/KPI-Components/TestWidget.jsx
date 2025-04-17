// import React from 'react';
import KPISection from './KPISection';
import StatsTrends from './StatsTrends';

// Dummy data
const stats = {
    totalPanels: 38,
    totalAmount: 4870,
    totalCarbon: 9.1,
    activeDonors: 12,
    newUsers: 5,
};

const chartData = [
    {
        date: "2024-01-10",
        user: "alice@example.com",
        country: "Kenya",
        panelType: "EcoSun-450W",
        quantity: 2,
        amount: 780,
        carbonOffset: "1.4",
    },
    {
        date: "2024-02-15",
        user: "bob@example.com",
        country: "India",
        panelType: "SolarMax-500W",
        quantity: 3,
        amount: 1170,
        carbonOffset: "2.1",
    },
    {
        date: "2024-02-28",
        user: "carol@example.com",
        country: "Kenya",
        panelType: "EcoSun-450W",
        quantity: 1,
        amount: 390,
        carbonOffset: "0.7",
    },
    {
        date: "2024-03-05",
        user: "dave@example.com",
        country: "Nigeria",
        panelType: "SunBright-420W",
        quantity: 4,
        amount: 1360,
        carbonOffset: "2.8",
    },
    {
        date: "2024-04-12",
        user: "emma@example.com",
        country: "Kenya",
        panelType: "EcoSun-450W",
        quantity: 3,
        amount: 1170,
        carbonOffset: "2.1",
    }
];

const TestWidget = () => {
    return (
        <div className="container py-4">
            <h1 className="mb-4 fw-bold text-center">Staff Dashboard</h1>
            <KPISection stats={stats} />
            <div className="mt-5">
                <StatsTrends data={chartData} />
            </div>
        </div>
    );
};

export default TestWidget;
