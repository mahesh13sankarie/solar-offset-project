import React from "react";

import './SideNav.css'; // Optional: for custom styling

const SideNav = () => {
    return (
        <div className="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark" style={{ width: '250px', height: '100vh' }}>
            <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">

                <span className="fs-4">User Name</span>
            </a>
            <hr />
            <ul className="nav nav-pills flex-column mb-auto">
                <li className="nav-item">
                    <a href="/dashboard" className="nav-link text-white" aria-current="page">
                        Dashboard
                    </a>
                </li>
                <li>
                    <a href="/dashboard/users" className="nav-link text-white">
                        Users
                    </a>
                </li>
                <li>
                    <a href="/settings" className="nav-link text-white">
                        Settings
                    </a>
                </li>
                <li>
                    <a href="/logs" className="nav-link text-white">
                        Logs
                    </a>
                </li>
                <li>
                    <a href="/logout" className="nav-link text-white">
                        Logout
                    </a>
                </li>
            </ul>
            <hr />
        </div>
    );
};

export default SideNav;