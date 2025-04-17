import React, { useState } from "react";
import "./StaffNavbarStyle.css" // Make sure this path is correct

export default function StaffNavBar() {
    const [activeTab, setActiveTab] = useState("report");

    return (
        <div className="side-nav">
            <nav className="navbar navbar-expand-sm text-light bg-dark">
                <a href="/" className="navbar-brand text-light" style={{ textDecoration: 'none', marginLeft: '20px' }}>
                    <i className="bi bi-arrow-left-circle"> Home</i>
                </a>
                <div className="container-fluid">
                    <ul className="navbar-nav d-flex justify-content-evenly w-100 nav-tab-list">
                        <li
                            className={`nav-tab-item ${activeTab === "report" ? "active-tab" : ""}`}
                            onClick={() => setActiveTab("report")}
                        >
                            Report Generate
                        </li>
                        <li
                            className={`nav-tab-item ${activeTab === "stats" ? "active-tab" : ""}`}
                            onClick={() => setActiveTab("stats")}
                        >
                            Statistics
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
    );
}