import React, { useEffect, useState } from "react";
import { FaSolarPanel } from "react-icons/fa";
import { Link } from "react-router-dom";
import { transactionEndpoints } from "../../api";
import Navbar from "../LandingPage/Navbar.jsx";

const COUNTRY_MAP = {
    GB: "United Kingdom",
    US: "United States",
    CA: "Canada",
    AU: "Australia",
    NZ: "New Zealand",
};

const TransactionHistory = () => {
    const [transactionData, setTransactionData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const userId = localStorage.getItem("userId");
        if (!userId) {
            setLoading(false);
            setLoading(false);
            return;
        }

        const fetchTransactions = async () => {
            try {
                const response = await transactionEndpoints.getUserTransactions(userId);
                setTransactionData(response.data);
                setLoading(false);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchTransactions();
    }, []);

    const getCountryName = (countryCode) => {
        return COUNTRY_MAP[countryCode] || countryCode;
    };

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
                                <th>#</th>
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
                                    <td>{index + 1}</td>
                                    <td>
                                        {
                                            new Date(transaction.payment.createdAt)
                                                .toISOString()
                                                .split("T")[0]
                                        }
                                    </td>
                                    <td>
                                        <div className="d-flex align-items-center">
                                            <FaSolarPanel
                                                style={{
                                                    fontSize: "1.2rem",
                                                    color: "#37517E",
                                                    marginRight: "10px",
                                                }}
                                            />
                                            {transaction.solarPanel.panelName}
                                        </div>
                                        <p
                                            style={{
                                                paddingTop: "5px",
                                                fontSize: "13px",
                                                fontFamily: "verdana",
                                            }}
                                        >
                                            {transaction.solarPanel.description}
                                        </p>
                                    </td>
                                    <td>{getCountryName(transaction.solarPanel.countryCode)}</td>
                                    <td>{transaction.solarPanel.productionPerPanel}W</td>
                                    <td>£{transaction.solarPanel.installationCost}</td>
                                    <td>
                                        {transaction.solarPanel.productionPerPanel *
                                            transaction.payment.amount *
                                            0.7}{" "}
                                        kg
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {loading ? (
                <div className="container text-center mt-5">
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                </div>
            ) : error ? (
                <div className="container text-center">No data found.</div>
            ) : transactionData.length === 0 ? (
                <div className="container text-center">No transactions found.</div>
            ) : (
                <></>
            )}
        </>
    );
};

export default TransactionHistory;
