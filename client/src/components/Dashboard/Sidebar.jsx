import React from "react";
import './SideNav.css'; // Optional: for custom styling
import { Link, useLocation } from 'react-router-dom';
import { FaHome, FaUser, FaCog, FaChartBar, FaSignOutAlt } from 'react-icons/fa'; // Importing icons

const SideNav = () => {
    const location = useLocation();
    const currentPath = location.pathname;

    const handleLogout = () => {
        const confirmLogout = window.confirm("Are you sure you want to log out?");
        if (confirmLogout) {
            localStorage.removeItem('authToken'); // Remove JWT token
            window.location.href = '/login'; // Force redirect to login
        }
    };

    return (
        <div className="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark" style={{ width: '250px', height: '100vh', borderRight: '2px solid #ddd' }}>
            <div className="d-flex align-items-center mb-4 text-white text-decoration-none">
                <span className="fs-4 fw-bold">Admin Panel</span>
            </div>
            <hr />
            <ul className="nav nav-pills flex-column mb-auto">
                <li className="nav-item">
                    <Link
                        to="/dashboard"
                        className={`nav-link text-white ${currentPath === '/dashboard' ? 'active bg-primary' : ''}`}
                    >
                        <FaHome className="me-2" /> Dashboard
                    </Link>
                </li>
                <li>
                    <Link
                        to="/dashboard/users"
                        className={`nav-link text-white ${currentPath === '/dashboard/users' ? 'active bg-primary' : ''}`}
                    >
                        <FaUser className="me-2" /> Users
                    </Link>
                </li>
                <li>
                    <Link
                        to="/dashboard/settings"
                        className={`nav-link text-white ${currentPath === '/dashboard/settings' ? 'active bg-primary' : ''}`}
                    >
                        <FaCog className="me-2" /> Settings
                    </Link>
                </li>
                <li>
                    <Link
                        to="/dashboard/reports"
                        className={`nav-link text-white ${currentPath === '/dashboard/logs' ? 'active bg-primary' : ''}`}
                    >
                        <FaChartBar className="me-2" /> Logs
                    </Link>
                </li>
                <li>
                    <Link
                        to="/logout"
                        className={`nav-link text-white ${currentPath === '/logout' ? 'active bg-primary' : ''}`}
                        onClick={handleLogout}
                    >
                        <FaSignOutAlt className="me-2" /> Logout
                    </Link>
                </li>
            </ul>
            <hr />
        </div>
    );
};

export default SideNav;