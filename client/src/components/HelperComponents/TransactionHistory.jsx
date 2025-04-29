import React from "react";
import { FaSolarPanel } from "react-icons/fa";
import Navbar from "../LandingPage/Navbar.jsx";  // Importing solar panel icon from react-icons
import { Link } from "react-router-dom";  // Import Link from react-router-dom

const transactionData = [
    {
        date: "2025-04-28",
        country: "France",
        panelType: "AIKO ABC Neostar 3N54 495W",
        quantity: 4,
        amount: 1560,
        carbonOffset: 1386.0
    },
    {
        date: "2025-04-29",
        country: "United Kingdom",
        panelType: "Project Solar Evo Max Super Series 480W",
        quantity: 3,
        amount: 1125,
        carbonOffset: 1008.0
    },
    {
        date: "2025-04-29",
        country: "United Kingdom",
        panelType: "Project Solar Evo Max Super Series 480W",
        quantity: 3,
        amount: 1125,
        carbonOffset: 1008.0
    },
    {
        date: "2025-04-29",
        country: "United Kingdom",
        panelType: "Project Solar Evo Max Super Series 480W",
        quantity: 3,
        amount: 1125,
        carbonOffset: 1008.0
    }
];

const TransactionHistory = () => {
    return (
        <>
            <Navbar />
            {/* Back Button */}
            <div className="container" style={{marginTop:"100px", marginLeft:"100px"}}>
                <Link
                    to="/SolarComparison"
                    className="text-decoration-none d-inline-flex align-items-center gap-2 mb-4 mt-3"
                    style={{ color: "#6c757d", fontWeight: "500", fontSize: "1rem" }}
                >
                    <i className="bi bi-arrow-left-circle" style={{ fontSize: "1.2rem" }}></i>
                    <span>Back</span>
                </Link>
            </div>


            {/* Transaction History Table */}
            <div className="container ">
                <h2 className="text-left mb-4" style={{ fontSize: "2rem", fontWeight: "600" }}>
                    Transaction History
                </h2>

                <div className="table-responsive">
                    <table className="table table-bordered table-striped table-hover">
                        <thead className="table-light">
                        <tr>
                            <th>Date</th>
                            <th>Country</th>
                            <th>Panel Type</th>
                            <th>Quantity</th>
                            <th>Amount</th>
                            <th>Carbon Offset</th>
                        </tr>
                        </thead>
                        <tbody>
                        {transactionData.map((transaction, index) => (
                            <tr key={index}>
                                <td>{transaction.date}</td>
                                <td>{transaction.country}</td>
                                <td>
                                    <div className="d-flex align-items-center">
                                        <FaSolarPanel style={{ fontSize: "1.2rem", color: "#37517E", marginRight: "10px" }} />
                                        {transaction.panelType}
                                    </div>
                                </td>
                                <td>{transaction.quantity}</td>
                                <td>${transaction.amount}</td>
                                <td>{transaction.carbonOffset} kg</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
};

export default TransactionHistory;