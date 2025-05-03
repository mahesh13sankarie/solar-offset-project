import React, {useEffect, useState} from "react";
import "./StaffNavbarStyle.css"

export default function StaffNavBar({activeTab,setActiveTab}) {
    const [staffName, setStaffName] = useState("");

    useEffect(() => {
        const fullName = localStorage.getItem("fullName");
        if (fullName) {
            setStaffName(fullName);
        }
    },[])

    return (
        <nav
            className="bg-dark text-light d-flex flex-column align-items-center p-3"
            style={{ width: '220px', height: '100vh' }}
        >
            <a
                href="/solarComparison"
                className="navbar-brand text-light mb-4 d-flex align-items-center"
                style={{ textDecoration: 'none' }}
            >
                <i className="bi bi-arrow-left-circle me-2"></i> Home
            </a>

            {/* Profile Section */}
            <div className="d-flex flex-column align-items-center mb-4">
                <i className="bi bi-person-circle" style={{ fontSize: "2rem" }}></i>
                <p className="m-2">{staffName || "Staff Name"}</p>
                <span className="badge bg-warning text-light-emphasis">Staff</span>
            </div>

            <ul className="nav flex-column align-items-center w-100 nav-tab-list">
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
                    <i className="bi bi-file-earmark-text-fill me-2"></i> Report
                </li>
                <li
                    className={`nav-tab-item mb-3 ps-2 ${activeTab === "enquires" ? "active-tab" : ""}`}
                    onClick={() => setActiveTab("enquires")}
                    style={{ cursor: 'pointer' }}
                >
                    <i className="bi bi-envelope me-2"></i> Enquires
                </li>
            </ul>
        </nav>
    );
}