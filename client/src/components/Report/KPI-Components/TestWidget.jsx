// import React from 'react';
import KPISection from "./KPISection";
import StatsTrends from "./StatsTrends";
import { useState, useEffect } from "react";
import axios from "axios";

const TestWidget = () => {
    const [stats, setStats] = useState({
        totalPanels: 0,
        totalAmount: 0,
        totalCarbon: 0,
        activeDonors: 0,
        newUsers: 0,
    });
    const [isFormalView, setIsFormalView] = useState(true);
    const [chartData, setChartData] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            console.error("No token found, please login first");
            setError("Authentication required. Please login.");
        }
        axios
            .get("http://localhost:8000/api/v1/transaction/staff", {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            })
            .then((response) => {
                const data = response.data;
                setChartData(data);

                const totalPanels = data.reduce((sum, item) => sum + item.quantity, 0);
                const totalAmount = data.reduce((sum, item) => sum + item.amount, 0);
                const totalCarbon = data.reduce((sum, item) => sum + item.carbonOffset, 0);
                const activeDonors = new Set(data.map((item) => item.user)).size;

                setStats({
                    totalPanels,
                    totalAmount,
                    totalCarbon: parseFloat(totalCarbon.toFixed(2)),
                    activeDonors,
                    newUsers: 0, // Placeholder for now if no new user metric is available
                });
            })
            .catch((error) => {
                console.error("Failed to fetch transaction data:", error);
            });
    }, []);

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
