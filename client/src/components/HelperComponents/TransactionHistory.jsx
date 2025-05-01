import React, { useState, useEffect } from "react";
import { FaSolarPanel } from "react-icons/fa";
import Navbar from "../LandingPage/Navbar.jsx";
import { Link } from "react-router-dom";

const TransactionHistory = () => {
    const [transactionData, setTransactionData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        const authToken = localStorage.getItem('token'); // Get the token from localStorage
        if (userId && authToken) {
            fetch(`http://localhost:8000/api/v1/transaction/${userId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${authToken}`, // Add the Bearer token
                    'Content-Type': 'application/json', // Optional: if your API expects this
                }
            })
                .then((response) => response.json())
                .then((data) => {
                    setTransactionData(data);
                    setLoading(false);
                })
                .catch((error) => {
                    setError(error);
                    setLoading(false);
                });
        }
    }, []);
    //
    // if (loading) return <div>Loading...</div>;


    return (
        <>
            <Navbar />
            <div className="container" style={{ marginTop: "100px", marginLeft: "100px" }}>
                <Link
                    to="/SolarComparison"
                    className="text-decoration-none d-inline-flex align-items-center gap-2 mb-4 mt-3"
                    style={{ color: "#6c757d", fontWeight: "500", fontSize: "1rem" }}
                >
                    <i className="bi bi-arrow-left-circle" style={{ fontSize: "1.2rem" }}></i>
                    <span>Back</span>
                </Link>
            </div>

            <div className="container">
                <h2 className="text-left mb-4" style={{ fontSize: "2rem", fontWeight: "600" }}>
                    Transaction History
                </h2>

                <div className="table-responsive">
                    <table className="table table-bordered table-striped table-hover">
                        <thead className="table-light">
                            <tr>
                                <th>Id</th>
                                <th>Date</th>

                                <th>Panel Name</th>
                                <th>Country</th>
                                <th>Production Per Panel</th>
                                <th>Amount</th>
                                <th>Carbon Offset</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactionData.map((transaction, index) => (
                                <tr key={index}>

                                    <td>{transaction.id}</td>
                                    <td>{new Date(transaction.payment.createdAt).toISOString().split('T')[0]}</td>
                                    <td>
                                        <div className="d-flex align-items-center">
                                            <FaSolarPanel style={{ fontSize: "1.2rem", color: "#37517E", marginRight: "10px" }} />
                                            {transaction.solarPanel.panelName}
                                        </div>
                                        <p style={{paddingTop:"5px", fontSize:"13px",fontFamily:"verdana"}}>{transaction.solarPanel.description}</p>
                                    </td>
                                    <td>{transaction.solarPanel.countryCode}</td>
                                    <td>{transaction.solarPanel.productionPerPanel}W</td>
                                    <td>${transaction.solarPanel.installationCost}</td>
                                    <td>{transaction.solarPanel.productionPerPanel * transaction.solarPanel.installationCost} kg</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {
                error ? (
                    <div className="container text-center">No data found.</div>
                ):(
                    <></>
                )
            }
        </>
    );
};

export default TransactionHistory;