import React, { useState } from "react";
import "./StaffNavbarStyle.css"

export default function StaffNavBar({activeTab,setActiveTab}) {

    return (
        <nav
            className="bg-dark text-light d-flex flex-column align-items-start p-3"
            style={{ width: '220px', height: '100vh' }}
        >
            <a
                href="/"
                className="navbar-brand text-light mb-4 d-flex align-items-center"
                style={{ textDecoration: 'none' }}
            >
                <i className="bi bi-arrow-left-circle me-2"></i> Home
            </a>
            <ul className="nav flex-column w-100 nav-tab-list">
                <li
                    className={`nav-tab-item mb-3 ps-2 ${activeTab === "stats" ? "active-tab" : ""}`}
                    onClick={() => setActiveTab("stats")}
                    style={{ cursor: 'pointer' }}
                >
                    <i className="bi bi-bar-chart-fill me-2"></i> Statistics
                </li>
                <li
                    className={`nav-tab-item mb-3 ps-2 ${activeTab === "report" ? "active-tab" : ""}`}
                    onClick={() => setActiveTab("report")}
                    style={{ cursor: 'pointer' }}
                >
                    <i className="bi bi-file-earmark-text-fill me-2"></i> Report Generate
                </li>
            </ul>
        </nav>
    );
}