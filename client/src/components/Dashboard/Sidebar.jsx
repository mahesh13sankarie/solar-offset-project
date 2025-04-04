import React from "react";

import './SideNav.css'; // Optional: for custom styling
import {Link, useLocation} from 'react-router-dom';

const SideNav = () => {

    const location = useLocation();
    const currentPath = location.pathname;
return (
    <div className="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark" style={{ width: '250px', height: '100vh' }}>
        <Link to="/dashboard" className="d-flex align-items-center mb-3 text-white text-decoration-none">
            <span className="fs-4">Admin Panel</span>
        </Link>
        <hr />
        <ul className="nav nav-pills flex-column mb-auto">
            <li className="nav-item">
                <Link
                    to="/dashboard"
                    className={`nav-link text-white ${currentPath === '/dashboard' ? 'active bg-primary' : ''}`}
                >
                    Dashboard
                </Link>
            </li>
            <li>
                <Link
                    to="/dashboard/users"
                    className={`nav-link text-white ${currentPath === '/dashboard/users' ? 'active bg-primary' : ''}`}
                >
                    Users
                </Link>
            </li>
            <li>
                <Link
                    to="/dashboard/settings"
                    className={`nav-link text-white ${currentPath === '/dashboard/settings' ? 'active bg-primary' : ''}`}
                >
                    Settings
                </Link>
            </li>
            <li>
                <Link
                    to="/dashboard/reports"
                    className={`nav-link text-white ${currentPath === '/dashboard/logs' ? 'active bg-primary' : ''}`}
                >
                    Logs
                </Link>
            </li>
            <li>
                <Link
                    to="/logout"
                    className={`nav-link text-white ${currentPath === '/logout' ? 'active bg-primary' : ''}`}
                    onClick={() => {
                        localStorage.removeItem('authToken'); // Remove JWT token
                        window.location.href = '/admin'; // Force redirect to login
                    }}
                >
                    Logout
                </Link>
            </li>
        </ul>
        <hr />
    </div>
);

};
export default SideNav;