import React, { useState, useEffect } from "react";
import axios from "axios";
import { api } from "../../api";
export function CountriesSelected(props) {
    const [countries, setCountries] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            console.error("No token found, please login first");
            setError("Authentication required. Please login.");
            return;
        }
        const fetchCountries = async () => {
            try {
                const response = await api.transaction.getStaff();
                const data = response.data;
                const countryCountMap = {};
                data.forEach((transaction) => {
                    const country = transaction.country;
                    countryCountMap[country] = (countryCountMap[country] || 0) + 1;
                });

                const sortedCountries = Object.entries(countryCountMap)
                    .sort((a, b) => b[1] - a[1])
                    .map(([name, count]) => ({ name, count }));

                setCountries(sortedCountries);
            } catch (error) {
                console.error("Error fetching countries:", error);
                setError("Failed to fetch countries");
            }
        };
        fetchCountries();
    }, []);

    return (
        <div className="container mt-4">
            <div className="card shadow-lg">
                <div className="card-header text-center">
                    <h3 className="card-title">Countries Selected by User</h3>
                    <p className="text-muted">Total Countries: {countries.length}</p>
                </div>

                <div
                    className="card-body"
                    style={{ height: "300px", overflowY: "auto", maxHeight: "300px" }}
                >
                    <table className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Country Name</th>
                                <th>Selections</th>
                            </tr>
                        </thead>
                        <tbody>
                            {countries.map((country, index) => (
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{country.name}</td>
                                    <td>{country.count}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="card-footer text-center">
                    <p className="text-muted">Displaying {countries.length} selected countries</p>
                    {/*<button className="btn btn-primary mt-2">Generate PDF</button>*/}
                </div>
            </div>
        </div>
    );
}

export function PanelsBought() {
    const [panels, setPanels] = useState([]);

    useEffect(() => {
        const fetchPanels = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                console.error("No token found, please login first");
                setError("Authentication required. Please login.");
                return;
            }

            try {
                const response = await api.transaction.getStaff();
                const data = response.data;
                const panelCountMap = {};

                data.forEach((transaction) => {
                    const { panelType, quantity, amount } = transaction;
                    if (panelCountMap[panelType]) {
                        panelCountMap[panelType].quantity += quantity;
                        panelCountMap[panelType].amount += amount;
                    } else {
                        panelCountMap[panelType] = { quantity, amount };
                    }
                });

                const sortedPanels = Object.entries(panelCountMap)
                    .sort((a, b) => b[1].quantity - a[1].quantity)
                    .map(([name, stats]) => ({ name, ...stats }));

                setPanels(sortedPanels);
            } catch (error) {
                console.error("Error fetching panels:", error);
            }
        };
        fetchPanels();
    }, []);

    return (
        <div className="container mt-4">
            <div className="card shadow-lg">
                <div className="card-header text-center">
                    <h3 className="card-title">Panels Bought</h3>
                    <p className="text-muted">Total Panels: {panels.length}</p>
                </div>

                <div
                    className="card-body"
                    style={{ height: "300px", overflowY: "auto", maxHeight: "300px" }}
                >
                    <table className="table table-striped table-bordered ">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Panel Name</th>
                                <th>Quantity</th>
                                <th>Total Cost</th>
                            </tr>
                        </thead>
                        <tbody>
                            {panels.map((panel, index) => (
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{panel.name}</td>
                                    <td>{panel.quantity}</td>
                                    <td>£{panel.amount.toFixed(2)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="card-footer text-center">
                    <p className="text-muted">Displaying {panels.length} panel types</p>
                    {/*<button className="btn btn-success mt-2">Download Report</button>*/}
                </div>
            </div>
        </div>
    );
}
